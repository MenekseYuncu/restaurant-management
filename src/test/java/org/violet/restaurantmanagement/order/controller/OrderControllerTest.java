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
import org.violet.restaurantmanagement.order.controller.request.OrderRequest;
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

import java.util.UUID;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest extends RmaControllerTest {

    @MockBean
    private OrderService orderService;

    private static final String BASE_URL = "/api/v1/order";

    @Test
    void givenOrderCreateRequest_whenCreateOrder_thenReturnsSuccess() throws Exception {
        // Given
        OrderRequest mockOrderRequest = new OrderRequest(
        );

        // When
        OrderCreateCommand createCommand = new OrderCreateCommand();
        Mockito.doNothing().when(orderService).createOrder(Mockito.any(OrderCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(orderService, Mockito.times(1))
                .createOrder(createCommand);
    }

    @Test
    void givenValidCancelOrderId_whenOrderFound_thenReturnSuccess() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();

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
    void givenInvalidOrderId_whenOrderNotFound_thenReturnOrderNotFoundException() throws Exception {
        // Given
        String orderId = UUID.randomUUID().toString();

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


}