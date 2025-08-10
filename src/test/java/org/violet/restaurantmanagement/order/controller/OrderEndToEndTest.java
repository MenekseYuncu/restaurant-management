package org.violet.restaurantmanagement.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaEndToEndTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

class OrderEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    private static final String BASE_URL = "/api/v1/order";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void givenValidCreateOrderRequest_thenReturnsSuccess() throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();

        DiningTableEntity table = diningTableRepository.save(
                DiningTableEntity.builder()
                        .mergeId(mergeId)
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );

        ProductEntity product = productRepository.save(
                ProductEntity.builder()
                        .categoryId(1L)
                        .name("Test")
                        .ingredient("ingredients")
                        .status(ProductStatus.ACTIVE)
                        .price(BigDecimal.valueOf(100))
                        .extent(100)
                        .extentType(ExtentType.GR)
                        .build()
        );

        List<OrderCreateRequest.ProductItem> productItems = List.of(
                new OrderCreateRequest.ProductItem(product.getId(), 2)
        );

        OrderCreateRequest mockCreateRequest = new OrderCreateRequest(
                table.getMergeId(),
                productItems
        );

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void givenValidCancelOrderId_whenOrderFound_thenReturnSuccess() throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();

        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .mergeId(mergeId)
                .status(OrderStatus.OPEN)
                .totalAmount(BigDecimal.valueOf(350))
                .build();

        OrderEntity savedOrder = orderRepository.save(order);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", savedOrder.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

}
