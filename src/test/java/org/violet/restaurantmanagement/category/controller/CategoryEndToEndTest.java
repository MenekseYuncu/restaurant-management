package org.violet.restaurantmanagement.category.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.Sorting;
import org.violet.restaurantmanagement.category.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.service.CategoryService;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.category.util.RmaEndToEndTest;
import org.violet.restaurantmanagement.category.util.RmaTestContainer;

import java.time.LocalDateTime;

class CategoryEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    @MockBean
    private CategoryService categoryService;

    private final static String BASE_URL = "/api/v1/category";

    @Test
    void testGetAllCategories() throws Exception {
        CategoryListCommand.CategoryFilter filter = CategoryListCommand.CategoryFilter.builder().name("Test").build();
        CategoryListRequest categoryListRequest = CategoryListRequest.builder()
                .pagination(Pagination.builder().pageNumber(2).pageSize(1).build())
                .sorting(Sorting.builder().direction(Sort.Direction.ASC).property("name").build())
                .filter(CategoryListRequest.CategoryFilter.builder().name("Category").build())
                .build();

        RmaPage<Category> rmaPage = RmaPage.<Category>builder()
                .pageNumber(1)
                .pageSize(2)
                .totalPageCount(1)
                .totalElementCount(3L)
                .sortedBy(categoryListRequest.getSorting())
                .filteredBy(filter)
                .build();

        Mockito.when(categoryService.getAllCategories(
                Mockito.any(CategoryListCommand.class))
        ).thenReturn(rmaPage);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryListRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = Category.builder()
                .id(1L)
                .name("Test")
                .status(CategoryStatus.ACTIVE)
                .createdAt(LocalDateTime.now()).build();

        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", 1L))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryCreateCommand command = new CategoryCreateCommand("Test", CategoryStatus.ACTIVE);

        Mockito.doNothing().when(categoryService).createCategory(Mockito.any(CategoryCreateCommand.class));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryUpdateCommand command = new CategoryUpdateCommand("Test", CategoryStatus.DELETED);

        Mockito.doNothing().when(categoryService).updateCategory(1L, command);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
