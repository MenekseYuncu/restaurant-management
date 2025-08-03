package org.violet.restaurantmanagement.order.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class OrderServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private DiningTableRepository diningTableRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;


    @Test
    void givenValidOrderCreateCommand_whenCreateOrder_thenSaveOrderAndItems() {

        // Given
        String mockMergeId = UUID.randomUUID().toString();

        ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productEntity.getId(), 2);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProductId(productEntity.getId());

        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .mergeId(mockMergeId)
                .price(productEntity.getPrice().multiply(BigDecimal.valueOf(productItem.quantity())))
                .build();

        // When
        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(true);

        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity.getId(), ProductStatus.DELETED))
                .thenReturn(true);

        Mockito.when(productRepository.findById(productEntity.getId()))
                .thenReturn(Optional.of(productEntity));

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(orderEntity);

        Order createdOrder = orderService.createOrder(createCommand);

        // Then
        Mockito.verify(diningTableRepository).existsByMergeId(mockMergeId);
        Mockito.verify(productRepository).findById(productEntity.getId());
        Mockito.verify(orderRepository).save(Mockito.any(OrderEntity.class));
        Mockito.verify(orderItemRepository).saveAll(Mockito.anyList());

        // Assertions
        Assertions.assertNotNull(createdOrder);
        Assertions.assertEquals(mockMergeId, createdOrder.getMergeId());
        Assertions.assertEquals(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP), createdOrder.getPrice());
    }


}