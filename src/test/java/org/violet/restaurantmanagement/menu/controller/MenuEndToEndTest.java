package org.violet.restaurantmanagement.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaEndToEndTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.menu.controller.request.MenuListRequest;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.util.Collections;

class MenuEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    private final static String BASE_URL = "/api/v1/menu";

    @Test
    void givenValidMenuListRequest_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest.MenuFilter mockFilter = MenuListRequest.MenuFilter.builder()
                .statuses(Collections.singleton(ProductStatus.ACTIVE))
                .build();
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
                        .build()
                )
                .filter(mockFilter)
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidMenuListRequestWithoutSorting_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest.MenuFilter mockFilter = MenuListRequest.MenuFilter.builder()
                .name("product")
                .build();
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .filter(mockFilter)
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidMenuListRequestWithoutFilter_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidMenuListRequestWithoutFilterAndSorting_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

}
