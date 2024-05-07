package org.violet.restaurantmanagement.product.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

import java.time.LocalDateTime;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryTest extends RMTest implements RestaurantManagementTestContainer {

    @LocalServerPort
    private int port;

    private String BASE_URL = "http://localhost";

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        BASE_URL = BASE_URL.concat(":").concat(port + "").concat("/api/v1/category");
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
