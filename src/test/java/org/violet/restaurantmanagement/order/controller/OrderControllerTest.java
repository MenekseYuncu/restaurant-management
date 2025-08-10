package org.violet.restaurantmanagement.order.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
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
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.exceptions.InvalidItemQuantityException;
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest extends RmaControllerTest {

    private static final String BASE_URL = "/api/v1/order";

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderDomainToOrderResponseMapper orderDomainToOrderResponseMapper;

    @MockBean
    private OrderCreateRequestToCommandMapper orderCreateRequestToCommandMapper;

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

        // When
        OrderCreateRequest mockOrderRequest = new OrderCreateRequest(mergeId, productItems);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder()
                        .productId(productEntity1.getId())
                        .quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .status(OrderItemStatus.PREPARING)
                        .build(),
                OrderItem.builder()
                        .productId(productEntity2.getId())
                        .quantity(1)
                        .price(BigDecimal.valueOf(150))
                        .status(OrderItemStatus.PREPARING)
                        .build()
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

        Mockito.when(orderService.createOrder(Mockito.any(OrderCreateCommand.class)))
                .thenReturn(mockOrder);
        Mockito.when(orderDomainToOrderResponseMapper.map(mockOrder))
                .thenReturn(mockOrderResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.orderId").value(mockOrder.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].productId").value(productEntity1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].quantity").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[1].productId").value(productEntity2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[1].quantity").value(1));

        // Verify
        Mockito.verify(orderService, Mockito.times(1))
                .createOrder(Mockito.any(OrderCreateCommand.class));
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

        // When
        Mockito.doThrow(new ProductNotFoundException()).when(orderService)
                .createOrder(Mockito.any(OrderCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Product does not exist"));

        // Verify
        ArgumentCaptor<OrderCreateCommand> commandCaptor = ArgumentCaptor.forClass(OrderCreateCommand.class);
        Mockito.verify(orderService).createOrder(commandCaptor.capture());
        OrderCreateCommand capturedCommand = commandCaptor.getValue();
        Assertions.assertEquals(mergeId, capturedCommand.mergeId());
        Assertions.assertEquals(invalidProductId, capturedCommand.products().getFirst().productId());
        Assertions.assertEquals(2, capturedCommand.products().getFirst().quantity());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0,
    })
    void givenInvalidProductQuantity_whenCreateOrder_thenBadRequest(int quantity) throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();
        ProductEntity productEntity1 = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .categoryId(1L)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build();

        List<OrderCreateRequest.ProductItem> productItems = List.of(
                new OrderCreateRequest.ProductItem(productEntity1.getId(), quantity)
        );

        // When
        OrderCreateRequest mockOrderRequest = new OrderCreateRequest(mergeId, productItems);

        Mockito.doThrow(new InvalidItemQuantityException()).when(orderService)
                .createOrder(Mockito.any(OrderCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        ArgumentCaptor<OrderCreateCommand> captor = ArgumentCaptor.forClass(OrderCreateCommand.class);
        Mockito.verify(orderService).createOrder(captor.capture());

        OrderCreateCommand cmd = captor.getValue();
        Assertions.assertEquals(mergeId, cmd.mergeId());
        Assertions.assertEquals(productEntity1.getId(), cmd.products().getFirst().productId());
        Assertions.assertEquals(quantity, cmd.products().getFirst().quantity());

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