package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.product.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {


    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/api/v1/category";


    @Test
    void givenGetCategoryById_whenValidInput_thenReturnSuccess() throws Exception {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Test");
        category.setStatus(CategoryStatus.ACTIVE);
        category.setCreatedAt(LocalDateTime.now());

        // When
        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenCreateCategory_whenValidInput_thenReturnsSuccess() throws Exception {
        // given
        CategoryCreateCommand command = new CategoryCreateCommand("Test", CategoryStatus.ACTIVE);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        Mockito.verify(categoryService, Mockito.times(1)).createCategory(command);
    }

    @Test
    void givenUpdateCategory_whenValidInput_thenReturnSuccess() throws Exception {
        //Given
        Long categoryId = 1L;
        CategoryUpdateRequest request = new CategoryUpdateRequest(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );

        // When
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Then
        CategoryUpdateCommand expectedCommand = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );
        Mockito.verify(categoryService).updateCategory(categoryId, expectedCommand);
    }

    @Test
    void givenDeleteCategory_thenReturnSuccess() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        doNothing().when(categoryService).deleteCategory(categoryId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/deleted/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }
}