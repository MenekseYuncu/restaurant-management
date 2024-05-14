package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductListRequest;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;
import org.violet.restaurantmanagement.util.RmaControllerTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class ProductControllerTest extends RmaControllerTest implements RmaTestContainer {

    @MockBean
    private ProductService productService;

    private final static String BASE_URL = "/api/v1/product";


    @Test
    void givenGetAllProducts_whenValidInput_thenReturnSuccess() throws Exception {
        // Given
        ProductListCommand.ProductFilter filter = ProductListCommand.ProductFilter.builder().name("Test").build();
        ProductListRequest productListRequest = ProductListRequest.builder()
                .pagination(Pagination.builder().pageNumber(2).pageSize(1).build())
                .sorting(Sorting.builder().direction(Sort.Direction.ASC).property("price").build())
                .filter(ProductListRequest.ProductFilter.builder()
                        .name("Product")
                        .statuses(Collections.singleton(ProductStatus.ACTIVE))
                        .build())
                .build();

        // When
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 1").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 2").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 3").status(ProductStatus.INACTIVE).build());

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(products)
                .pageNumber(1)
                .pageSize(3)
                .totalPageCount(1)
                .totalElementCount(3L)
                .sortedBy(productListRequest.getSorting())
                .filteredBy(filter)
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.name")
                        .value(filter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenGetAllProducts_whenProductListWithoutSorting_thenReturnSuccess() throws Exception {
        // Given
        ProductListCommand.ProductFilter filter = ProductListCommand.ProductFilter.builder().name("Test").build();
        ProductListRequest productListRequest = ProductListRequest.builder()
                .pagination(Pagination.builder().pageNumber(2).pageSize(1).build())
                .filter(ProductListRequest.ProductFilter.builder()
                        .name("Product")
                        .categoryId(1L)
                        .build())
                .build();

        // When
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 1").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 2").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 3").status(ProductStatus.ACTIVE).build());

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(products)
                .pageNumber(1)
                .pageSize(3)
                .totalPageCount(1)
                .totalElementCount(3L)
                .filteredBy(filter)
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.name")
                        .value(filter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenGetAllProducts_whenProductListWithoutSortingAndFiltering_thenReturnSuccess() throws Exception {
        // Given
        ProductListRequest productListRequest = ProductListRequest.builder()
                .pagination(Pagination.builder().pageNumber(2).pageSize(1).build())
                .build();

        // When
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 1").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 2").status(ProductStatus.ACTIVE).build());
        products.add(Product.builder().id(String.valueOf(UUID.randomUUID())).name("product 3").status(ProductStatus.ACTIVE).build());

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(products)
                .pageNumber(1)
                .pageSize(3)
                .totalPageCount(1)
                .totalElementCount(3L)
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenGetAllProduct_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest productListRequest = ProductListRequest.builder().build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenGetAllProduct_whenInvalidNegativePageSize_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest productListRequest = ProductListRequest.builder()
                .pagination(Pagination.builder().pageSize(-1).pageNumber(1).build())
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenGetAllProducts_whenInvalidNegativePageNumber_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest productListRequest = ProductListRequest.builder()
                .pagination(Pagination.builder().pageSize(1).pageNumber(-1).build())
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void givenGetProductById_whenValidInput_thenReturnSuccess() throws Exception {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";

        // When
        Product product = Product.builder()
                .id(productId)
                .categoryId(1L)
                .name("Test")
                .ingredient("ingredients")
                .status(ProductStatus.ACTIVE)
                .price(BigDecimal.valueOf(100))
                .extent(100)
                .extentType(ExtentType.GR)
                .createdAt(LocalDateTime.now()).build();

        Mockito.when(productService.getProductById(productId)).thenReturn(product);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name")
                        .value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(productService, Mockito.times(1)).getProductById(productId);
    }

    @Test
    void givenGetProductById_whenInvalidProductId_thenReturnNotFound() throws Exception {
        // Given
        String productId = "1L";

        // When
        Mockito.when(productService.getProductById(productId))
                .thenThrow(new ProductNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(productService, Mockito.times(1)).getProductById(productId);
    }

    @Test
    void givenGetProductId_whenProductByIdEmpty_thenException() throws Exception {
        // Given
        String productId = "";

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

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

    @Test
    void givenUpdateProduct_whenValidInput_thenReturnSuccess() throws Exception {
        //Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";

        // When
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        Mockito.doNothing().when(productService).updateProduct(productId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateCommand)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(productService).updateProduct(productId, updateCommand);
    }

    @Test
    void givenUpdateProduct_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductUpdateCommand command = new ProductUpdateCommand(
                null,
                "ingredients",
                null,
                ProductStatus.ACTIVE,
                null,
                ExtentType.GR
        );

        //When
        Mockito.doThrow(new ProductNotFoundException()).when(productService)
                .updateProduct(productId, command);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }


    @Test
    void givenUpdateProduct_whenProductIdEmpty_thenException() throws Exception {
        // Given
        String productId = "";

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenUpdateProduct_whenInvalidId_thenReturnBadRequest() throws Exception {
        //Given
        String productId = "invalidProductId";

        // When
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .updateProduct(productId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenDeleteProduct_thenReturnSuccess() throws Exception {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";

        // When
        Mockito.doNothing().when(productService).deleteProduct(productId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(productService).deleteProduct(productId);
    }

    @Test
    void givenDeleteProduct_whenInvalidId_thenReturnNotFound() throws Exception {
        // Given
        String productId = "1L";

        // When
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .deleteProduct(productId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(productService).deleteProduct(productId);
    }

    @Test
    void givenDeleteProduct_whenProductIdEmpty_thenException() throws Exception {
        // Given
        String productId = "";

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}