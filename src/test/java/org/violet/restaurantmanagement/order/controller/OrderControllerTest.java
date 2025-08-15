package org.violet.restaurantmanagement.order.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.order.controller.mapper.OrderCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.order.controller.mapper.OrderDomainToOrderResponseMapper;
import org.violet.restaurantmanagement.order.controller.mapper.OrderRemoveItemRequestToCommandMapper;
import org.violet.restaurantmanagement.order.controller.mapper.OrderUpdateRequestToCommandMapper;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.controller.request.OrderRemoveItemRequest;
import org.violet.restaurantmanagement.order.controller.request.OrderUpdateRequest;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.exceptions.InvalidItemQuantityException;
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.command.OrderRemoveItemCommand;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest extends RmaControllerTest {

    private static final String BASE_URL = "/api/v1/order";

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderDomainToOrderResponseMapper orderDomainToOrderResponseMapper;

    @MockBean
    private OrderCreateRequestToCommandMapper orderCreateRequestToCommandMapper;

    @MockBean
    private OrderUpdateRequestToCommandMapper orderUpdateRequestToCommandMapper;

    @MockBean
    private OrderRemoveItemRequestToCommandMapper orderRemoveItemRequestToCommandMapper;


    @Test
    void givenOrderCreateRequest_whenCreateOrder_thenReturnsSuccess() throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();
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

        List<OrderCreateRequest.ProductItem> productItems = List.of(
                new OrderCreateRequest.ProductItem(productEntity1.getId(), 2),
                new OrderCreateRequest.ProductItem(productEntity2.getId(), 1)
        );

        OrderCreateRequest mockOrderRequest = new OrderCreateRequest(mergeId, productItems);
        OrderCreateCommand command = new OrderCreateCommand(mergeId, null);

        Mockito.when(orderCreateRequestToCommandMapper.map(any(OrderCreateRequest.class)))
                .thenReturn(command);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder()
                        .productId(productEntity1.getId()).quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .status(OrderItemStatus.PREPARING).build(),
                OrderItem.builder()
                        .productId(productEntity2.getId()).quantity(1)
                        .price(BigDecimal.valueOf(150))
                        .status(OrderItemStatus.PREPARING).build()
        );

        Order mockOrder = new Order(
                UUID.randomUUID().toString(),
                mergeId,
                OrderStatus.OPEN,
                BigDecimal.valueOf(350),
                orderItems,
                LocalDateTime.now(),
                null
        );

        Mockito.when(orderService.createOrder(any(OrderCreateCommand.class)))
                .thenReturn(mockOrder);

        OrderResponse mockOrderResponse = new OrderResponse(
                mockOrder.getId(),
                mockOrder.getStatus(),
                List.of(
                        new OrderResponse.OrderProductResponse(productEntity1.getId(), productEntity1.getName(), 2, productEntity1.getPrice()),
                        new OrderResponse.OrderProductResponse(productEntity2.getId(), productEntity2.getName(), 1, productEntity2.getPrice())
                ),
                mockOrder.getTotalAmount(),
                mockOrder.getCreatedAt(),
                mockOrder.getUpdatedAt()
        );

        Mockito.when(orderDomainToOrderResponseMapper.map(any(Order.class)))
                .thenReturn(mockOrderResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.orderId").value(mockOrder.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].productId").value(productEntity1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalAmount").value(350.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.updatedAt").isEmpty());

        // Verify
        Mockito.verify(orderService, Mockito.times(1))
                .createOrder(any(OrderCreateCommand.class));
    }

    @Test
    void givenInvalidMergeId_whenCreateOrder_thenBadRequest() throws Exception {
        // Given
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build();

        List<OrderCreateRequest.ProductItem> productItems = List.of(
                new OrderCreateRequest.ProductItem(productEntity1.getId(), 2)
        );

        // When
        OrderCreateRequest mockOrderRequest = new OrderCreateRequest(null, productItems);

        OrderCreateCommand mockOrderCreateCommand = orderCreateRequestToCommandMapper.map(mockOrderRequest);

        Mockito.doThrow(new DiningTableNotFoundException()).when(orderService)
                .createOrder(mockOrderCreateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void givenInvalidProductId_whenCreateOrder_thenNotFound() throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();
        String invalidProductId = "invalid-product-id";

        List<OrderCreateRequest.ProductItem> productItems = List.of(
                new OrderCreateRequest.ProductItem(invalidProductId, 2)
        );

        OrderCreateRequest mockOrderRequest = new OrderCreateRequest(mergeId, productItems);

        OrderCreateCommand createCommand = new OrderCreateCommand(
                mergeId,
                List.of(new OrderCreateCommand.ProductItem(invalidProductId, 2))
        );

        Mockito.when(orderCreateRequestToCommandMapper.map(any(OrderCreateRequest.class)))
                .thenReturn(createCommand);

        Mockito.when(orderService.createOrder(any()))
                .thenThrow(new ProductNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Product does not exist"));

        // Verify
        ArgumentCaptor<OrderCreateCommand> captor = ArgumentCaptor.forClass(OrderCreateCommand.class);
        Mockito.verify(orderService).createOrder(captor.capture());
        OrderCreateCommand captured = captor.getValue();
        Assertions.assertEquals(mergeId, captured.mergeId());
        Assertions.assertEquals(invalidProductId, captured.products().getFirst().id());
        Assertions.assertEquals(2, captured.products().getFirst().quantity());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0,
    })
    void givenInvalidProductQuantity_whenCreateOrder_thenBadRequest(int quantity) throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                mergeId,
                List.of(new OrderCreateRequest.ProductItem(productId, quantity))
        );

        OrderCreateCommand createCommand = new OrderCreateCommand(
                mergeId,
                List.of(new OrderCreateCommand.ProductItem(productId, quantity))
        );

        // When
        Mockito.when(orderCreateRequestToCommandMapper.map(any(OrderCreateRequest.class)))
                .thenReturn(createCommand);

        Mockito.when(orderService.createOrder(any()))
                .thenThrow(new InvalidItemQuantityException());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"));


        //verify
        var captor = ArgumentCaptor.forClass(OrderCreateCommand.class);
        Mockito.verify(orderService).createOrder(captor.capture());
        var captured = captor.getValue();
        Assertions.assertEquals(mergeId, captured.mergeId());
        Assertions.assertEquals(productId, captured.products().getFirst().id());
        Assertions.assertEquals(quantity, captured.products().getFirst().quantity());
    }


    @Test
    void givenValidUpdateRequest_whenUpdateOrder_thenReturnUpdatedOrder() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .name("Product 1")
                .build();
        ProductEntity productEntity2 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(150))
                .name("Product 2")
                .build();

        // When
        OrderUpdateRequest.ProductItem productItem = new OrderUpdateRequest.ProductItem(productEntity1.getId(), 1);
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(List.of(productItem));

        OrderUpdateCommand.ProductItem productItems = new OrderUpdateCommand.ProductItem(productEntity1.getId(), 1);
        OrderUpdateCommand command = new OrderUpdateCommand(List.of(productItems));

        Mockito.when(orderUpdateRequestToCommandMapper.map(any(OrderUpdateRequest.class)))
                .thenReturn(command);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder()
                        .productId(productEntity1.getId()).quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .status(OrderItemStatus.PREPARING).build(),
                OrderItem.builder()
                        .productId(productEntity2.getId()).quantity(1)
                        .price(BigDecimal.valueOf(150))
                        .status(OrderItemStatus.PREPARING).build()
        );

        Order mockOrder = new Order(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                OrderStatus.OPEN,
                BigDecimal.valueOf(350),
                orderItems,
                LocalDateTime.now(),
                null
        );

        Mockito.when(orderService.updateOrder(ArgumentMatchers.eq(orderId), any(OrderUpdateCommand.class)))
                .thenReturn(mockOrder);

        OrderResponse mockOrderResponse = new OrderResponse(
                mockOrder.getId(),
                mockOrder.getStatus(),
                List.of(
                        new OrderResponse.OrderProductResponse(productEntity1.getId(), productEntity1.getName(), 2, productEntity1.getPrice()),
                        new OrderResponse.OrderProductResponse(productEntity2.getId(), productEntity2.getName(), 1, productEntity2.getPrice())
                ),
                mockOrder.getTotalAmount(),
                mockOrder.getCreatedAt(),
                mockOrder.getUpdatedAt()
        );

        Mockito.when(orderDomainToOrderResponseMapper.map(any(Order.class)))
                .thenReturn(mockOrderResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.orderId").value(mockOrder.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].productId").value(productEntity1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalAmount").value(350.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.updatedAt").isEmpty());

        // Verify
        Mockito.verify(orderService, Mockito.times(1))
                .updateOrder(ArgumentMatchers.eq(orderId), any(OrderUpdateCommand.class));
    }

    @Test
    void givenInvalidOrderId_whenUpdateOrder_thenReturnNotFound() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .name("Product 1")
                .build();

        OrderUpdateRequest.ProductItem productItem = new OrderUpdateRequest.ProductItem(productEntity1.getId(), 1);
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(List.of(productItem));

        // When
        OrderUpdateCommand.ProductItem productItems = new OrderUpdateCommand.ProductItem(productEntity1.getId(), 1);
        OrderUpdateCommand command = new OrderUpdateCommand(List.of(productItems));

        Mockito.when(orderUpdateRequestToCommandMapper.map(any(OrderUpdateRequest.class)))
                .thenReturn(command);

        Mockito.when(orderService.updateOrder(ArgumentMatchers.eq(orderId), any(OrderUpdateCommand.class)))
                .thenThrow(new OrderNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Order not found"));
    }

    @Test
    void givenInvalidProductId_whenUpdateOrder_thenReturnBadRequest() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity invalidProductEntity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .name("Invalid Product")
                .build();

        // When
        OrderUpdateRequest.ProductItem invalidProductItem = new OrderUpdateRequest.ProductItem(invalidProductEntity.getId(), 1);
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(List.of(invalidProductItem));

        Mockito.when(orderUpdateRequestToCommandMapper.map(any(OrderUpdateRequest.class)))
                .thenThrow(new ProductNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Product does not exist"));
    }

    @Test
    void givenMissingProductInformation_whenUpdateOrder_thenReturnBadRequest() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();

        // When
        OrderUpdateRequest.ProductItem invalidProductItem = new OrderUpdateRequest.ProductItem(null, 1);
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(List.of(invalidProductItem));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Validation failed"));
    }

    @Test
    void givenInvalidProductQuantity_whenUpdateOrder_thenReturnBadRequest() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .name("Product 1")
                .build();

        // When
        OrderUpdateRequest.ProductItem invalidProductItem = new OrderUpdateRequest.ProductItem(productEntity1.getId(), 0);
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(List.of(invalidProductItem));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Validation failed"));
    }


    @Test
    void givenValidOrderRemoveItemRequest_whenRemoveItemProductFromOrder_thenUpdateQuantity() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(100))
                .name("Product 1")
                .build();
        ProductEntity productEntity2 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(150))
                .name("Product 2")
                .build();

        // When
        OrderRemoveItemRequest.ProductItem productItem = new OrderRemoveItemRequest.ProductItem(productEntity1.getId(), 1);
        OrderRemoveItemRequest removeItemRequest = new OrderRemoveItemRequest(List.of(productItem));

        OrderRemoveItemCommand.ProductItem productItems = new OrderRemoveItemCommand.ProductItem(productEntity1.getId(), 1);
        OrderRemoveItemCommand command = new OrderRemoveItemCommand(List.of(productItems));

        Mockito.when(orderRemoveItemRequestToCommandMapper.map(any(OrderRemoveItemRequest.class)))
                .thenReturn(command);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder()
                        .productId(productEntity1.getId()).quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .status(OrderItemStatus.PREPARING).build(),
                OrderItem.builder()
                        .productId(productEntity2.getId()).quantity(1)
                        .price(BigDecimal.valueOf(150))
                        .status(OrderItemStatus.PREPARING).build()
        );

        Order mockOrder = new Order(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                OrderStatus.OPEN,
                BigDecimal.valueOf(350),
                orderItems,
                LocalDateTime.now(),
                null
        );

        // When
        Mockito.when(orderService.removeItemProductsFromOrder(ArgumentMatchers.eq(orderId), ArgumentMatchers.any(OrderRemoveItemCommand.class)))
                .thenReturn(mockOrder);

        OrderResponse mockOrderResponse = new OrderResponse(
                mockOrder.getId(),
                mockOrder.getStatus(),
                List.of(
                        new OrderResponse.OrderProductResponse(productEntity1.getId(), productEntity1.getName(), 2, productEntity1.getPrice()),
                        new OrderResponse.OrderProductResponse(productEntity2.getId(), productEntity2.getName(), 1, productEntity2.getPrice())
                ),
                mockOrder.getTotalAmount(),
                mockOrder.getCreatedAt(),
                mockOrder.getUpdatedAt()
        );

        Mockito.when(orderDomainToOrderResponseMapper.map(any(Order.class)))
                .thenReturn(mockOrderResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/remove", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(removeItemRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.orderId").value(mockOrder.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].productId").value(productEntity1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalAmount").value(350.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.updatedAt").isEmpty());

        Mockito.verify(orderService, Mockito.times(1))
                .removeItemProductsFromOrder(ArgumentMatchers.eq(orderId), ArgumentMatchers.any(OrderRemoveItemCommand.class));

        Mockito.verify(orderRemoveItemRequestToCommandMapper, Mockito.times(1))
                .map(any(OrderRemoveItemRequest.class));

        Mockito.verify(orderDomainToOrderResponseMapper, Mockito.times(1))
                .map(any(Order.class));
    }

    @Test
    void givenInvalidProductId_whenRemoveItemProductFromOrder_thenNotFound() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        String invalidProductId = "invalid-product-id";

        List<OrderRemoveItemRequest.ProductItem> productItems = List.of(
                new OrderRemoveItemRequest.ProductItem(invalidProductId, 2)
        );

        OrderRemoveItemRequest mockOrderRequest = new OrderRemoveItemRequest(productItems);

        OrderRemoveItemCommand removeItemCommand = new OrderRemoveItemCommand(
                List.of(new OrderRemoveItemCommand.ProductItem(invalidProductId, 2))
        );

        // When
        Mockito.when(orderRemoveItemRequestToCommandMapper.map(any(OrderRemoveItemRequest.class)))
                .thenReturn(removeItemCommand);

        Mockito.when(orderService.removeItemProductsFromOrder(orderId, removeItemCommand))
                .thenThrow(new ProductNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/remove", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Product does not exist"));

        // Verify
        Mockito.verify(orderService, Mockito.times(1))
                .removeItemProductsFromOrder(ArgumentMatchers.eq(orderId), ArgumentMatchers.any(OrderRemoveItemCommand.class));

        Mockito.verify(orderRemoveItemRequestToCommandMapper, Mockito.times(1))
                .map(any(OrderRemoveItemRequest.class));
    }

    @Test
    void givenInvalidQuantity_whenRemoveItemProductFromOrder_thenBadRequest() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        List<OrderRemoveItemRequest.ProductItem> productItems = List.of(
                new OrderRemoveItemRequest.ProductItem(productId, -1)
        );

        OrderRemoveItemRequest mockOrderRequest = new OrderRemoveItemRequest(productItems);

        OrderRemoveItemCommand removeItemCommand = new OrderRemoveItemCommand(
                List.of(new OrderRemoveItemCommand.ProductItem(productId, -1))
        );

        // When
        Mockito.when(orderRemoveItemRequestToCommandMapper.map(any(OrderRemoveItemRequest.class)))
                .thenReturn(removeItemCommand);

        Mockito.when(orderService.removeItemProductsFromOrder(orderId, removeItemCommand))
                .thenThrow(new BadRequestException("Validation failed"));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/remove", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Validation failed"));
    }

    @Test
    void givenMissingProductId_whenRemoveItemProductFromOrder_thenBadRequest() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();

        List<OrderRemoveItemRequest.ProductItem> productItems = List.of(
                new OrderRemoveItemRequest.ProductItem("", 2)
        );

        OrderRemoveItemRequest mockOrderRequest = new OrderRemoveItemRequest(productItems);

        OrderRemoveItemCommand removeItemCommand = new OrderRemoveItemCommand(
                List.of(new OrderRemoveItemCommand.ProductItem("", 2))
        );

        // When
        Mockito.when(orderRemoveItemRequestToCommandMapper.map(any(OrderRemoveItemRequest.class)))
                .thenReturn(removeItemCommand);

        Mockito.when(orderService.removeItemProductsFromOrder(orderId, removeItemCommand))
                .thenThrow(new BadRequestException("Validation failed"));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/remove", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Validation failed"));
    }

    @Test
    void givenValidCancelOrderId_whenOrderFound_thenReturnSuccess() throws Exception {
        // Given
        String orderId = String.valueOf(UUID.randomUUID());

        // When
        Mockito.doNothing().when(orderService).cancelOrder(orderId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", orderId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(orderService).cancelOrder(orderId);
    }

    @Test
    void givenInvalidCancelOrderId_whenOrderNotExist_thenReturnNotFound() throws Exception {
        // Given
        String orderId = "1L";

        // When
        Mockito.doThrow(OrderNotFoundException.class)
                .when(orderService)
                .cancelOrder(orderId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", orderId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(orderService).cancelOrder(orderId);
    }

    @Test
    void givenOrderIdEmpty_whenCancelOrderNotFound_thenException() throws Exception {
        // Given
        String orderId = "";

        // When
        Mockito.doThrow(OrderNotFoundException.class)
                .when(orderService)
                .cancelOrder(orderId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", orderId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void givenOrderIdNotFound_whenCancelProduct_thenReturnNotFoundException() throws Exception {
        // Given
        String orderId = String.valueOf(UUID.randomUUID());

        // When
        Mockito.doThrow(OrderNotFoundException.class)
                .when(orderService)
                .cancelOrder(orderId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", orderId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(orderService, Mockito.times(1)).cancelOrder(orderId);
    }

}