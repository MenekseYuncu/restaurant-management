package org.violet.restaurantmanagement.order.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.order.controller.mapper.OrderDomainToOrderResponseMapper;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
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

}