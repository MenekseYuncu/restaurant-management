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
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.exceptions.OrderUpdateNotAllowedException;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.exceptions.ProductStatusAlreadyChanged;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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


    @Test
    void givenValidUpdateCommand_whenUpdateOrder_thenSaveOrderWithNewItems() {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .build();

        OrderEntity existingOrder = OrderEntity.builder()
                .id(orderId)
                .status(OrderStatus.IN_PROGRESS)
                .totalAmount(BigDecimal.valueOf(200))
                .items(new ArrayList<>())
                .build();

        OrderUpdateCommand.ProductItem productItem = new OrderUpdateCommand.ProductItem(productEntity.getId(), 2);
        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of(productItem));

        OrderEntity updatedOrder = OrderEntity.builder()
                .id(orderId)
                .status(OrderStatus.OPEN)
                .totalAmount(BigDecimal.valueOf(400.00))
                .items(new ArrayList<>())
                .build();

        // When
        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(existingOrder));

        Mockito.when(productRepository.findById(productEntity.getId()))
                .thenReturn(Optional.of(productEntity));

        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity.getId(), ProductStatus.DELETED))
                .thenReturn(true);

        Mockito.when(orderRepository.findByIdWithItems(orderId))
                .thenReturn(Optional.of(updatedOrder));

        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class)))
                .thenReturn(updatedOrder);

        Mockito.when(orderItemRepository.saveAll(Mockito.anyList()))
                .thenReturn(new ArrayList<>());

        // Then
        Order updated = orderService.updateOrder(updatedOrder.getId(), updateCommand);

        // Assertion
        Assertions.assertEquals(BigDecimal.valueOf(400.00).setScale(2, RoundingMode.HALF_UP),
                updated.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void givenInvalidOrderId_whenUpdateOrder_thenThrowOrderNotFoundException() {
        // Given
        String invalidOrderId = UUID.randomUUID().toString();
        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of());

        Mockito.when(orderRepository.findById(invalidOrderId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrder(invalidOrderId, updateCommand));
    }

    @Test
    void givenCompletedOrder_whenUpdateOrder_thenThrowOrderUpdateNotAllowedException() {
        // Given
        OrderEntity completedOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.COMPLETED)
                .totalAmount(BigDecimal.valueOf(300))
                .build();

        OrderUpdateCommand.ProductItem productItem = new OrderUpdateCommand.ProductItem(UUID.randomUUID().toString(), 1);
        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of(productItem));

        Mockito.when(orderRepository.findById(completedOrder.getId()))
                .thenReturn(Optional.of(completedOrder));

        // Then
        Assertions.assertThrows(OrderUpdateNotAllowedException.class,
                () -> orderService.updateOrder(completedOrder.getId(), updateCommand));
    }

    @Test
    void givenEmptyProductList_whenUpdateOrder_thenThrowProductNotFoundException() {
        // Given
        OrderEntity existingOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .totalAmount(BigDecimal.valueOf(300))
                .build();

        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of());

        Mockito.when(orderRepository.findById(existingOrder.getId()))
                .thenReturn(Optional.of(existingOrder));

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> orderService.updateOrder(existingOrder.getId(), updateCommand));
    }

    @Test
    void givenInvalidProductQuantity_whenUpdateOrder_thenThrowInvalidItemQuantityException() {
        // Given
        ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .build();

        OrderEntity existingOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .totalAmount(BigDecimal.valueOf(300))
                .build();

        OrderUpdateCommand.ProductItem productItem = new OrderUpdateCommand.ProductItem(productEntity.getId(), -1);
        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of(productItem));

        // When
        Mockito.when(productRepository.existsByIdAndStatusNot(productEntity.getId(), ProductStatus.DELETED))
                .thenReturn(true);

        Mockito.when(orderRepository.findById(existingOrder.getId()))
                .thenReturn(Optional.of(existingOrder));

        // Assertion
        Assertions.assertThrows(InvalidItemQuantityException.class,
                () -> orderService.updateOrder(existingOrder.getId(), updateCommand));
    }

    @Test
    void givenDeletedProduct_whenUpdateOrder_thenThrowProductNotFoundException() {
        // Given
        OrderEntity existingOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .totalAmount(BigDecimal.valueOf(300))
                .build();

        String deletedProductId = UUID.randomUUID().toString();

        OrderUpdateCommand.ProductItem productItem = new OrderUpdateCommand.ProductItem(deletedProductId, 1);
        OrderUpdateCommand updateCommand = new OrderUpdateCommand(List.of(productItem));

        // When
        Mockito.when(orderRepository.findById(existingOrder.getId()))
                .thenReturn(Optional.of(existingOrder));

        Mockito.when(productRepository.existsByIdAndStatusNot(deletedProductId, ProductStatus.DELETED))
                .thenReturn(false);

        // Assertion
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> orderService.updateOrder(existingOrder.getId(), updateCommand));
    }


    @Test
    void givenCancelOrder_whenOrderExists_thenReturnSuccess() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.OPEN)
                .mergeId(mockMergeId)
                .totalAmount(BigDecimal.valueOf(200))
                .build();

        // When
        Mockito.when(orderRepository.findById(orderEntity.getId()))
                .thenReturn(Optional.of(orderEntity));

        orderService.cancelOrder(orderEntity.getId());

        // Verify
        Mockito.verify(orderRepository, Mockito.times(1))
                .save(orderEntity);

        // Assert
        Assertions.assertEquals(OrderStatus.CANCELED, orderEntity.getStatus());
    }

    @Test
    void givenCanceledOrder_whenOrderAlreadyCanceled_thenThrowException() {
        // Given
        String mockMergeId = UUID.randomUUID().toString();
        OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .status(OrderStatus.CANCELED)
                .mergeId(mockMergeId)
                .totalAmount(BigDecimal.valueOf(200))
                .build();

        // When
        Mockito.when(orderRepository.findById(orderEntity.getId()))
                .thenReturn(Optional.of(orderEntity));

        // Then
        Assertions.assertThrows(ProductStatusAlreadyChanged.class,
                () -> orderService.cancelOrder(orderEntity.getId()));

        // Verify
        Mockito.verify(orderRepository, Mockito.times(0))
                .save(orderEntity);
    }

    @Test
    void givenOrderIdDoesNotExists_whenCanceledOrder_thenThrowOrderNotFoundException() {
        //Given
        String orderId = UUID.randomUUID().toString();

        //When
        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderService.cancelOrder(orderId));

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(ArgumentMatchers.any(OrderEntity.class));
    }
}