package org.violet.restaurantmanagement.category.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaEndToEndTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.SortingBuilder;

class CategoryEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    @Autowired
    private CategoryRepository categoryRepository;
    private final static String BASE_URL = "/api/v1/category";

    @Test
    void whenCategoryListRequestExist_thenReturnCategories() throws Exception {
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("name")
                        .build()
                )
                .filter(CategoryListRequest.CategoryFilter.builder()
                        .name("Category")
                        .build()
                )
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(mockCategoryListRequest.getPagination().getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(mockCategoryListRequest.getPagination().getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }
    @Test
    void testGetCategoryById() throws Exception {
        Long categoryId = 1L;

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(categoryEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(categoryEntity.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.status").value(categoryEntity.getStatus()
                        .toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenCategoryCreateRequestExist_thenReturnSuccess() throws Exception {
        CategoryCreateRequest mockCategoryCreateRequest = new CategoryCreateRequest(
                "Category",
                CategoryStatus.ACTIVE
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategoryCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertTrue(categoryRepository.existsByName(mockCategoryCreateRequest.name()));
    }

    @Test
    void whenCategoryIdExist_thenUpdateCategory() throws Exception {
        Long categoryId = 1L;

        CategoryUpdateRequest mockCategoryUpdateRequest = new CategoryUpdateRequest(
                "Test",
                CategoryStatus.DELETED
        );

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategoryUpdateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenCategoryIdExist_thenDeleteCategory() throws Exception {
        Long categoryId = 1L;

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        categoryEntity.setStatus(CategoryStatus.DELETED);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
