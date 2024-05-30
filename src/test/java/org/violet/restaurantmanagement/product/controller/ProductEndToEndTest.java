package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaEndToEndTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductListRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductUpdateRequest;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.model.mapper.ProductDomainToProductEntityMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class ProductEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    @Autowired
    private ProductRepository productRepository;

    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/product";

    @Test
    void whenProductListRequestExist_thenReturnProducts() throws Exception {
        // Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(1)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("price")
                        .build()
                )
                .filter(ProductListRequest.ProductFilter.builder()
                        .name("Product")
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenProductListRequestExistWithoutSorting_thenReturnProducts() throws Exception {
        // Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(1)
                        .pageNumber(1)
                        .build()
                )
                .filter(ProductListRequest.ProductFilter.builder()
                        .name("Product")
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenProductListRequestExistWithoutFilter_thenReturnProducts() throws Exception {
        //Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(1)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("price")
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenProductListRequestExistWithoutSortingAndFilter_thenReturnProducts() throws Exception {
        // Gİven
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(1)
                        .pageNumber(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidProductId_whenProductByIdFound_thenReturnSuccess() throws Exception {
        // Given
        Product product = Product.builder()
                .categoryId(1L)
                .name("Test")
                .ingredient("ingredients")
                .status(ProductStatus.ACTIVE)
                .price(BigDecimal.valueOf(100))
                .extent(100)
                .extentType(ExtentType.GR)
                .createdAt(LocalDateTime.now()).build();

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);
        productRepository.save(productEntity);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productEntity.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name")
                        .value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.ingredient")
                        .value(product.getIngredient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.status")
                        .value(product.getStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.price")
                        .value(product.getPrice().doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.extent")
                        .value(product.getExtent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.extentType")
                        .value(product.getExtentType().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidCreateProductRequest_thenReturnsSuccess() throws Exception {
        // Given
        ProductCreateRequest mockCreateRequest = new ProductCreateRequest(
                1L,
                "Product Test",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenValidUpdateProductRequest_whenProductFound_thenReturnSuccess() throws Exception {
        // Given
        Product product = Product.builder()
                .categoryId(1L)
                .name("Test products")
                .ingredient("ingredients")
                .status(ProductStatus.INACTIVE)
                .price(BigDecimal.valueOf(100))
                .extent(100)
                .extentType(ExtentType.GR)
                .createdAt(LocalDateTime.now()).build();

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);
        productRepository.save(productEntity);

        // When
        ProductUpdateRequest mockUpdateRequest = new ProductUpdateRequest(
                1L,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockUpdateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenValidDeleteProductId_whenProductFound_thenReturnSuccess() throws Exception {
        // Given
        Product product = Product.builder()
                .categoryId(1L)
                .name("Test delete")
                .ingredient("ingredients")
                .status(ProductStatus.ACTIVE)
                .price(BigDecimal.valueOf(100))
                .extent(100)
                .extentType(ExtentType.GR)
                .createdAt(LocalDateTime.now()).build();

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);
        productRepository.save(productEntity);


        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productEntity.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }
}
