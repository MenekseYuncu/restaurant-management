package org.violet.restaurantmanagement.order.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.violet.restaurantmanagement.order.controller.request.OrderUpdateRequest;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;
import org.violet.restaurantmanagement.order.model.mapper.OrderItemDomainToEntityMapperImpl;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class OrderEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    private static final String BASE_URL = "/api/v1/order";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @MockBean
    private OrderItemDomainToEntityMapperImpl orderItemDomainToEntityMapper;

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
    void givenValidUpdateOrderRequest_whenUpdateOrder_thenReturnsUpdatedOrder() throws Exception {
        // Given
        DiningTableEntity table = diningTableRepository.save(
                DiningTableEntity.builder()
                        .mergeId(UUID.randomUUID().toString())
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );

        ProductEntity productEntity1 = productRepository.save(
                ProductEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .price(BigDecimal.valueOf(100))
                        .name("Product 1")
                        .ingredient("ingredients")
                        .extent(1)
                        .extentType(ExtentType.ML)
                        .status(ProductStatus.ACTIVE)
                        .build()
        );
        ProductEntity productEntity2 = productRepository.save(
                ProductEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .price(BigDecimal.valueOf(150))
                        .name("Product 2")
                        .ingredient("ingredients")
                        .extent(1)
                        .extentType(ExtentType.ML)
                        .status(ProductStatus.ACTIVE)
                        .build()
        );

        List<OrderUpdateRequest.ProductItem> productItems = List.of(
                new OrderUpdateRequest.ProductItem(productEntity1.getId(), 1),
                new OrderUpdateRequest.ProductItem(productEntity2.getId(), 1)
        );

        OrderUpdateRequest updateRequest = new OrderUpdateRequest(productItems);

        OrderEntity order = orderRepository.save(
                OrderEntity.builder()
                        .mergeId(table.getMergeId())
                        .status(OrderStatus.OPEN)
                        .totalAmount(BigDecimal.valueOf(350))
                        .build()
        );

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

        orderItemRepository.saveAll(orderItemDomainToEntityMapper.map(orderItems));

        // When
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.orderId").value(order.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].productId").value(productEntity1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.products[0].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalAmount").value(600.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.updatedAt").isNotEmpty());

        // Verify
        Optional<OrderEntity> updatedOrderOpt = orderRepository.findById(order.getId());
        Assertions.assertTrue(updatedOrderOpt.isPresent());
        OrderEntity updatedOrder = updatedOrderOpt.get();
        Assertions.assertNotNull(updatedOrder.getUpdatedAt());
        Assertions.assertEquals(OrderStatus.IN_PROGRESS, updatedOrder.getStatus());
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
