package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryToCategoryResponseMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateRequestToUpdateCommandMapper;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {


    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private static final CategoryToCategoryResponseMapper toCategoryResponseMapper = CategoryToCategoryResponseMapper.INSTANCE;
    private static final CategoryCreateRequestToCreateCommandMapper toCreateCommandMapper = CategoryCreateRequestToCreateCommandMapper.INSTANCE;
    private static final CategoryUpdateRequestToUpdateCommandMapper toUpdateCommandMapper = CategoryUpdateRequestToUpdateCommandMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/category";

    @Test
    void whenCreateCategory_ValidInput_ReturnsSuccess() throws Exception {
        // given
        CategoryCreateCommand command = new CategoryCreateCommand("Test", CategoryStatus.ACTIVE);

        //when
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(status().isOk());

        // then
        verify(categoryService, times(1)).createCategory(command);
    }
}
