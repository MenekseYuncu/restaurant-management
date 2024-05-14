package org.violet.restaurantmanagement.product.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.util.RmaEndToEndTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.math.BigDecimal;

class ProductEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    @MockBean
    private ProductService productService;

    private final static String BASE_URL = "/api/v1/product";

    @Test
    void testCreateProduct() throws Exception {
        ProductCreateCommand command = new ProductCreateCommand(
                1L,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        Mockito.doNothing().when(productService).createProduct(Mockito.any(ProductCreateCommand.class));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateProduct() throws Exception {
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );
        Mockito.doNothing().when(productService).updateProduct(productId, updateCommand);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct( "5f98b326-b5db-4b71-bdb0-8eed335fd6e4");

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}",  "5f98b326-b5db-4b71-bdb0-8eed335fd6e4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
