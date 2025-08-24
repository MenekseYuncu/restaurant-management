package org.violet.restaurantmanagement.order.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.order.exceptions.*;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.model.mapper.OrderDomainToEntityMapper;
import org.violet.restaurantmanagement.order.model.mapper.OrderEntityToDomainMapper;
import org.violet.restaurantmanagement.order.model.mapper.OrderItemDomainToEntityMapper;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.command.OrderRemoveItemCommand;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.command.ProductLine;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.payment.repository.PaymentRepository;
import org.violet.restaurantmanagement.payment.repository.entity.PaymentEntity;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {

    private static final OrderDomainToEntityMapper orderDomainToEntityMapper = OrderDomainToEntityMapper.INSTANCE;
    private static final OrderItemDomainToEntityMapper orderItemDomainToEntityMapper = OrderItemDomainToEntityMapper.INSTANCE;
    private static final OrderEntityToDomainMapper orderEntityToDomainMapper = OrderEntityToDomainMapper.INSTANCE;
    private final OrderRepository orderRepository;
    private final DiningTableRepository diningTableRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<Order> getOrdersByMergeId(String mergeId) {
        this.checkExistingDiningTable(mergeId);

        List<OrderEntity> orders = orderRepository.findAllByMergeIdOrderByCreatedAtDesc(mergeId);

        return orders.stream()
                .map(orderEntityToDomainMapper::map)
                .toList();
    }

    @Override
    public Order createOrder(final OrderCreateCommand createCommand) {
        this.checkExistingDiningTable(createCommand.mergeId());
        this.validateProducts(createCommand.products());

        if (createCommand.products().isEmpty()) {
            throw new ProductNotFoundException();
        }

        List<OrderItem> orderItems = this.createOrderItems(createCommand.products());

        Order order = Order.builder()
                .mergeId(createCommand.mergeId())
                .status(OrderStatus.OPEN)
                .build();

        OrderEntity orderEntity = orderDomainToEntityMapper.map(order);

        List<OrderItemEntity> itemEntities = orderItems.stream()
                .map(orderItemDomainToEntityMapper::map)
                .peek(item -> item.setOrder(orderEntity))
                .toList();

        final List<DiningTableEntity> tables = diningTableRepository.findByMergeId(order.getMergeId());

        orderEntity.addItems(itemEntities, tables);

        final Order savedOrder = orderEntityToDomainMapper.map(orderRepository.save(orderEntity));
        PaymentEntity payment = PaymentEntity.buildPayment(orderEntity);
        paymentRepository.save(payment);

        return savedOrder;
    }

    @Override
    @Transactional
    public Order updateOrder(final String id, final OrderUpdateCommand updateCommand) {
        final OrderEntity order = findOrderById(id);

        this.validateProducts(updateCommand.products());

        List<OrderItem> newItems = this.createOrderItems(updateCommand.products());

        List<OrderItemEntity> newOrderItems = newItems.stream()
                .map(orderItemDomainToEntityMapper::map)
                .peek(item -> item.setOrder(order))
                .toList();

        order.updateItems(newOrderItems);

        orderItemRepository.saveAll(newOrderItems);
        OrderEntity saved = orderRepository.save(order);

        final OrderEntity orderWithItems = fetchOrderWithItems(saved);

        return orderEntityToDomainMapper.map(orderWithItems);
    }

    @Transactional
    public Order removeItemProductsFromOrder(final String id, final OrderRemoveItemCommand removeItemCommand) {
        final OrderEntity order = findOrderById(id);

        if (order.isNotUpdatable()) {
            throw new OrderUpdateNotAllowedException();
        }

        if (removeItemCommand.products().isEmpty()) {
            throw new ProductNotFoundException();
        }

        removeItemCommand.products().forEach(productItem -> {
            try {
                order.removeItem(productItem.id(), productItem.quantity());
            } catch (ProductNotFoundException e) {
                throw new ProductNotFoundException();
            }
        });

        OrderEntity savedOrder = orderRepository.save(order);

        final OrderEntity orderWithItems = fetchOrderWithItems(savedOrder);

        return orderEntityToDomainMapper.map(orderWithItems);
    }


    private OrderEntity fetchOrderWithItems(OrderEntity order) {
        return orderRepository.findByIdWithItems(order.getId())
                .orElseThrow(OrderNotFoundException::new);
    }

    public List<OrderItem> createOrderItems(final List<? extends ProductLine> items) {
        return items.stream()
                .map(this::toOrderItem)
                .toList();
    }

    private OrderItem toOrderItem(ProductLine line) {
        if (line.quantity() < 1) throw new InvalidItemQuantityException();

        ProductEntity product = productRepository.findById(line.id())
                .orElseThrow(ProductNotFoundException::new);

        return OrderItem.builder()
                .productId(product.getId())
                .price(product.getPrice())
                .quantity(line.quantity())
                .status(OrderItemStatus.PREPARING)
                .build();
    }

    private void checkExistingDiningTable(final String mergeId) {
        if (mergeId == null || mergeId.isBlank()) {
            throw new MergeIdNotFoundException();
        }
        boolean isTableNotPresent = !diningTableRepository.existsByMergeId(mergeId);
        if (isTableNotPresent) {
            throw new DiningTableNotFoundException();
        }
    }

    private void validateProducts(final List<? extends ProductLine> items) {
        items.forEach(line -> {
            if (!productRepository.existsByIdAndStatusNot(line.id(), ProductStatus.DELETED)) {
                throw new ProductNotFoundException();
            }
        });
    }

    @Override
    public void cancelOrder(final String id) {
        final OrderEntity order = findOrderById(id);

        this.checkExistingStatus(order.getStatus());
        order.cancel();
        orderRepository.save(order);
    }

    private OrderEntity findOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    private OrderItemEntity findOrderItemById(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(OrderItemNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteCanceledOrders() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<OrderEntity> canceledOrders = orderRepository.findAllCanceledOrdersOlderThan7Days(sevenDaysAgo);

        for (OrderEntity order : canceledOrders) {
            try {
                orderRepository.delete(order);
            } catch (Exception e) {
                log.error("Error deleting order with ID {}: {}", order.getId(), e.getMessage());
                throw new OrderDeletionException();
            }
        }
    }

    private void checkExistingStatus(final OrderStatus currentStatus) {
        if (currentStatus == OrderStatus.CANCELED) {
            throw new StatusAlreadyChangedException();
        }
    }

    @Override
    public void changeOrderItemStatusToDelivered(String id) {
        final OrderItemEntity orderItemEntity = findOrderItemById(id);

        this.checkExistingOrderItemStatus(orderItemEntity.getStatus(), OrderItemStatus.DELIVERED);
        orderItemEntity.delivered();
        orderItemRepository.save(orderItemEntity);
    }

    @Override
    public void changeOrderItemStatusToReady(String id) {
        final OrderItemEntity orderItemEntity = findOrderItemById(id);

        this.checkExistingOrderItemStatus(orderItemEntity.getStatus(), OrderItemStatus.READY);
        orderItemEntity.ready();
        orderItemRepository.save(orderItemEntity);
    }

    @Override
    public void changeOrderItemStatusToCancelled(String id) {
        final OrderItemEntity orderItemEntity = findOrderItemById(id);

        this.checkExistingOrderItemStatus(orderItemEntity.getStatus(), OrderItemStatus.CANCELED);
        orderItemEntity.cancel();
        orderItemRepository.save(orderItemEntity);
    }


    private void checkExistingOrderItemStatus(OrderItemStatus currentStatus, OrderItemStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new StatusAlreadyChangedException();
        }
    }

}
