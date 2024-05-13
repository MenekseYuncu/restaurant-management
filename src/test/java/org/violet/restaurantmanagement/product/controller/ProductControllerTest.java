package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.util.RmaControllerTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.math.BigDecimal;

class ProductControllerTest extends RmaControllerTest implements RmaTestContainer {

    @MockBean
    private ProductService productService;

    private final static String BASE_URL = "/api/v1/product";


    @Test
    void givenCreateProduct_whenValidInput_thenReturnsSuccess() throws Exception {
        // Given
        ProductCreateRequest request = new ProductCreateRequest(
                1L,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        ProductCreateCommand createCommand = new ProductCreateCommand(
                request.categoryId(),
                request.name(),
                request.ingredient(),
                request.price(),
                request.status(),
                request.extent(),
                request.extentType()
        );
        // When
        Mockito.doNothing().when(productService).createProduct(Mockito.any(ProductCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(productService, Mockito.times(1)).createProduct(createCommand);
    }

    @Test
    void givenCreateProduct_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        ProductCreateCommand command = new ProductCreateCommand(
                null,
                null,
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.doThrow(new ProductNotFoundException()).when(productService).createProduct(command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }
}