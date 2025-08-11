package org.violet.restaurantmanagement.order.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.order.exceptions.InvalidItemQuantityException;
import org.violet.restaurantmanagement.order.exceptions.MergeIdNotFoundException;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
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
        orderItemEntity.setProduct(ProductEntity.builder().id(productEntity.getId()).build());

        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .mergeId(mockMergeId)
                .totalAmount(productEntity.getPrice().multiply(BigDecimal.valueOf(productItem.quantity())))
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
        Assertions.assertEquals(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP), createdOrder.getTotalAmount());
    }

    @Test
    void givenMultipleProducts_whenCreateOrder_thenCorrectTotalPriceCalculated() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();

        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build();

        ProductEntity productEntity2 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(2L)
                .name("Product 2")
                .price(BigDecimal.valueOf(150))
                .build();

        OrderCreateCommand.ProductItem productItem1 = new OrderCreateCommand.ProductItem(productEntity1.getId(), 2);
        OrderCreateCommand.ProductItem productItem2 = new OrderCreateCommand.ProductItem(productEntity2.getId(), 1);

        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem1, productItem2));

        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .mergeId(mockMergeId)
                .totalAmount(BigDecimal.valueOf(350))
                .build();

        // When
        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(true);

        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity1.getId(), ProductStatus.DELETED))
                .thenReturn(true);
        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity2.getId(), ProductStatus.DELETED))
                .thenReturn(true);

        Mockito.when(productRepository.findById(productEntity1.getId()))
                .thenReturn(Optional.of(productEntity1));

        Mockito.when(productRepository.findById(productEntity2.getId()))
                .thenReturn(Optional.of(productEntity2));

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(orderEntity);

        // When
        Order createdOrder = orderService.createOrder(createCommand);

        // Assertions
        Assertions.assertEquals(BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_UP), createdOrder.getTotalAmount());
    }

    @Test
    void givenDeletedProduct_whenCreateOrder_thenThrowProductNotFoundException() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productId, 1);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(true);

        Mockito.when(productRepository.existsByIdAndStatusNot(productId, ProductStatus.DELETED))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(createCommand));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any(OrderEntity.class));
    }

    @Test
    void givenInvalidMergeIdCreateCommand_whenCreateOrder_thenThrowException() {
        // Given
        String mockMergeId = "";
        String productId = UUID.randomUUID().toString();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productId, 2);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        // Assert
        Assertions.assertThrows(MergeIdNotFoundException.class,
                () -> orderService.createOrder(createCommand));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(ArgumentMatchers.any(OrderEntity.class));
    }

    @Test
    void givenEmptyProductList_whenCreateOrder_thenThrowProductNotFoundException() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of());

        // When
        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(createCommand));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any(OrderEntity.class));
    }

    @Test
    void givenInvalidProductQuantity_whenCreateOrder_thenThrowInvalidItemQuantityException() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productEntity.getId(), 0);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        // When
        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(true);

        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity.getId(), ProductStatus.DELETED))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(InvalidItemQuantityException.class,
                () -> orderService.createOrder(createCommand));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any(OrderEntity.class));
    }


    @Test
    void givenNonexistentDiningTable_whenCreateOrder_thenThrowDiningTableNotFoundException() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productId, 1);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        Mockito.when(diningTableRepository.existsByMergeId(mockMergeId))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> orderService.createOrder(createCommand));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any(OrderEntity.class));
    }

    @Test
    void givenDiscountedProducts_whenCreateOrder_thenCalculateCorrectTotalPrice() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();

        ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Discounted Product")
                .price(BigDecimal.valueOf(50))
                .build();

        OrderCreateCommand.ProductItem productItem = new OrderCreateCommand.ProductItem(productEntity.getId(), 4);
        OrderCreateCommand createCommand = new OrderCreateCommand(mockMergeId, List.of(productItem));

        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .mergeId(mockMergeId)
                .totalAmount(BigDecimal.valueOf(200))
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

        // When
        Order createdOrder = orderService.createOrder(createCommand);

        // Assertions
        Assertions.assertEquals(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP), createdOrder.getTotalAmount());
    }
}