package org.violet.restaurantmanagement.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.model.mapper.OrderDomainToEntityMapper;
import org.violet.restaurantmanagement.order.model.mapper.OrderItemDomainToEntityMapper;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
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

    private final OrderRepository orderRepository;
    private final DiningTableRepository diningTableRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(final OrderCreateCommand createCommand) {
        this.checkExistingDiningTable(createCommand.mergeId());

        this.validateProducts(createCommand.products());

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

    private List<OrderItem> createOrderItems(final List<OrderCreateCommand.ProductItem> productItems) {
        return productItems.stream()
                .map(item -> {
                    ProductEntity product = productRepository.findById(item.productId())
                            .orElseThrow(ProductNotFoundException::new);

                    return OrderItem.builder()
                            .productId(product.getId())
                            .price(product.getPrice())
                            .quantity(item.quantity())
                            .status(OrderItemStatus.PREPARING)
                            .build();
                })
                .toList();
    }

    private void saveOrderItems(final List<OrderItem> orderItems,
                                final String orderId
    ) {
        List<OrderItemEntity> itemEntities = orderItems.stream()
                .map(item -> {
                    OrderItemEntity entity = orderItemDomainToEntityMapper.map(item);
                    entity.setOrderId(orderId);
                    return entity;
                })
                .toList();

        orderItemRepository.saveAll(itemEntities);
    }

    private void checkExistingDiningTable(final String mergeId) {
        boolean isTableNotPresent = !diningTableRepository.existsByMergeId(mergeId);
        if (isTableNotPresent) {
            throw new DiningTableNotFoundException();
        }
    }

    private void validateProducts(final List<OrderCreateCommand.ProductItem> productItems) {
        productItems.forEach(item -> {
            boolean productExists = productRepository.existsByIdAndStatusNot(
                    item.productId(),
                    ProductStatus.DELETED
            );
            if (!productExists) {
                throw new ProductNotFoundException();
            }
        });
    }

    private BigDecimal calculateTotalPrice(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void cancelOrder(String id) {

    }
}
