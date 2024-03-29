package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
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
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

import java.time.LocalDateTime;

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

        // When
        Category category = Category.builder()
                .id(categoryId)
                .name("Test")
                .status(CategoryStatus.ACTIVE)
                .createdAt(LocalDateTime.now()).build();

        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name")
                        .value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
    }

    @Test
    void givenGetCategoryById_whenInvalidNegativeInput_thenReturnBadRequest() throws Exception {
        //Given
        Long categoryId = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .getCategoryById(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenGetCategoryById_whenInvalidCategoryId_thenReturnNotFound() throws Exception {
        // Given
        Long categoryId = 999L;

        // When
        Mockito.when(categoryService.getCategoryById(categoryId))
                .thenThrow(new CategoryNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
    }


    @Test
    void givenCreateCategory_whenValidInput_thenReturnsSuccess() throws Exception {
        // Given
        CategoryCreateCommand command = new CategoryCreateCommand("Test", CategoryStatus.ACTIVE);

        // When
        Mockito.doNothing().when(categoryService).createCategory(Mockito.any(CategoryCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).createCategory(command);
    }

    @Test
    void givenCreateCategory_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        CategoryCreateCommand command = new CategoryCreateCommand(null, null);

        // When
        Mockito.doThrow(new CategoryNotFoundException()).when(categoryService).createCategory(command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenUpdateCategory_whenValidInput_thenReturnSuccess() throws Exception {
        //Given
        Long categoryId = 1L;

        // When
        CategoryUpdateCommand command = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );

        Mockito.doNothing().when(categoryService).updateCategory(categoryId, command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(categoryService).updateCategory(categoryId, command);
    }

    @Test
    void givenUpdateCategory_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryUpdateCommand command = new CategoryUpdateCommand(null, null);

        //When
        Mockito.doThrow(new CategoryNotFoundException()).when(categoryService)
                .updateCategory(categoryId, command);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenUpdateCategory_whenInvalidNegativeInput_thenReturnBadRequest() throws Exception {
        //Given
        Long categoryId = -1L;

        // When
        CategoryUpdateCommand command = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );
        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .updateCategory(categoryId, command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenDeleteCategory_thenReturnSuccess() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        Mockito.doNothing().when(categoryService).deleteCategory(categoryId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void givenDeleteCategory_whenInvalidId_thenReturnNotFound() throws Exception {
        // Given
        Long categoryId = 100L;

        // When
        Mockito.doThrow(CategoryNotFoundException.class)
                .when(categoryService)
                .deleteCategory(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(categoryService).deleteCategory(categoryId);
    }



    @Test
    void givenDeleteCategory_whenInvalidNegativeInput_thenReturnBadRequest() throws Exception {
        // Given
        Long categoryId = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .deleteCategory(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }
}