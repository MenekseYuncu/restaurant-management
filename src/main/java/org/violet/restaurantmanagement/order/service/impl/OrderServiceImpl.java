package org.violet.restaurantmanagement.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.order.exceptions.InvalidItemQuantityException;
import org.violet.restaurantmanagement.order.exceptions.MergeIdNotFoundException;
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.exceptions.OrderUpdateNotAllowedException;
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
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.command.ProductLine;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.exceptions.ProductStatusAlreadyChanged;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public Order createOrder(final OrderCreateCommand createCommand) {
        this.checkExistingDiningTable(createCommand.mergeId());

        this.validateProducts(createCommand.products());

        if (createCommand.products().isEmpty()) {
            throw new ProductNotFoundException();
        }

        List<OrderItem> orderItems = this.createOrderItems(createCommand.products());

        BigDecimal totalPrice = calculateTotalPrice(orderItems);

        Order order = Order.builder()
                .mergeId(createCommand.mergeId())
                .status(OrderStatus.OPEN)
                .price(totalPrice)
                .items(orderItems)
                .build();

        OrderEntity savedOrder = orderRepository.save(orderDomainToEntityMapper.map(order));
        order.setId(savedOrder.getId());
        order.setCreatedAt(savedOrder.getCreatedAt());
        this.saveOrderItems(orderItems, savedOrder.getId());

        return order;
    }

    @Override
    public Order updateOrder(final String id, final OrderUpdateCommand updateCommand) {
        OrderEntity existingOrder = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        if (existingOrder.getStatus() == OrderStatus.CANCELED ||
                existingOrder.getStatus() == OrderStatus.COMPLETED) {
            throw new OrderUpdateNotAllowedException();
        }

        this.validateProducts(updateCommand.products());

        if (updateCommand.products().isEmpty()) {
            throw new ProductNotFoundException();
        }

        List<OrderItem> newItems = this.createOrderItems(updateCommand.products());

        BigDecimal newItemsTotal = calculateTotalPrice(newItems);
        BigDecimal currentTotal = existingOrder.getTotalAmount() != null
                ? existingOrder.getTotalAmount()
                : BigDecimal.ZERO;

        existingOrder.setTotalAmount(currentTotal.add(newItemsTotal));

        this.saveOrderItems(newItems, id);

        OrderEntity saved = orderRepository.save(existingOrder);

        OrderEntity orderWithItems = orderRepository.findByIdWithItems(saved.getId())
                .orElseThrow(OrderNotFoundException::new);

        return orderEntityToDomainMapper.map(orderWithItems);
    }

    private List<OrderItem> createOrderItems(final List<? extends ProductLine> items) {
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

    private void saveOrderItems(final List<OrderItem> orderItems,
                                final String orderId
    ) {
        List<OrderItemEntity> itemEntities = orderItems.stream()
                .map(item -> {
                    OrderItemEntity entity = orderItemDomainToEntityMapper.map(item);
                    entity.setOrder(OrderEntity.builder().id(orderId).build());
                    return entity;
                })
                .toList();

        orderItemRepository.saveAll(itemEntities);
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

    private BigDecimal calculateTotalPrice(final List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void cancelOrder(final String id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        this.checkExistingStatus(order.getStatus());
        order.cancel();
        orderRepository.save(order);
    }

    private void checkExistingStatus(OrderStatus currentStatus) {
        if (currentStatus == OrderStatus.CANCELED) {
            throw new ProductStatusAlreadyChanged();
        }
    }
}
