package org.violet.restaurantmanagement.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.product.controller.mapper.ProductCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductUpdateRequestToProductUpdateCommandMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductListRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductUpdateRequest;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ProductControllerTest extends RmaControllerTest implements RmaTestContainer {

    @MockBean
    private ProductService productService;
    private static final ProductUpdateRequestToProductUpdateCommandMapper productUpdateRequestToCommandMapper = ProductUpdateRequestToProductUpdateCommandMapper.INSTANCE;
    private static final ProductCreateRequestToCreateCommandMapper productCreateRequestToCommandMapper = ProductCreateRequestToCreateCommandMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/product";


    @Test
    void givenValidProductListRequest_whenProductsFound_thenReturnSuccess() throws Exception {
        // Given
        ProductListRequest.ProductFilter mockFilter = ProductListRequest.ProductFilter.builder()
                .name("Product")
                .build();
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("price")
                        .build()
                )
                .filter(mockFilter)
                .build();

        // When
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 1")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockProductListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockProductListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .sortedBy(mockProductListRequest.getSorting())
                .filteredBy(mockFilter)
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
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
                        .value(mockFilter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(productService, Mockito.times(1))
                .getAllProducts(Mockito.any(ProductListCommand.class));

    }

    @Test
    void givenProductListWithoutSorting_whenGetAllProductsFound_thenReturnSuccess() throws Exception {
        // Given
        ProductListRequest.ProductFilter mockFilter = ProductListRequest.ProductFilter.builder()
                .name("Product")
                .build();
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .filter(mockFilter)
                .build();

        // When
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 1")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockProductListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockProductListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .filteredBy(mockFilter)
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
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
                        .value(mockFilter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenProductListWithoutSortingAndFiltering_whenGetAllProductsFound_thenReturnSuccess() throws Exception {
        // Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .build();

        // When
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 1")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockProductListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockProductListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .build();

        Mockito.when(productService.getAllProducts(
                Mockito.any(ProductListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
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
    void givenInvalidInput_whenGetAllProduct_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest productListRequest = ProductListRequest.builder().build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenInvalidNegativePageSize_whenGetAllProduct_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(-1)
                        .pageNumber(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenInvalidNegativePageNumber_whenGetAllProducts_thenReturnBadRequest() throws Exception {
        // Given
        ProductListRequest mockProductListRequest = ProductListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(-1)
                        .pageSize(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProductListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenValidProductId_whenProductByIdFound_thenReturnSuccess() throws Exception {
        // Given
        String productId = String.valueOf(UUID.randomUUID());

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
                .andDo(MockMvcResultHandlers.print())
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
    void givenInvalidProductId_whenProductById_thenReturnNotFound() throws Exception {
        // Given
        String productId = "1L";

        // When
        Mockito.when(productService.getProductById(productId))
                .thenThrow(new ProductNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(productService, Mockito.times(1)).getProductById(productId);
    }

    @Test
    void givenProductByIdEmpty_whenProductByIdNotExist_thenThrowException() throws Exception {
        // Given
        String productId = null;

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenValidCreateProductRequest_whenCreateProduct_thenReturnsSuccess() throws Exception {
        // Given
        ProductCreateRequest mockCreateRequest = new ProductCreateRequest(
                1L,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        ProductCreateCommand productCreateCommand = productCreateRequestToCommandMapper.map(mockCreateRequest);

        Mockito.doNothing().when(productService).createProduct(Mockito.any(ProductCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(productService, Mockito.times(1))
                .createProduct(productCreateCommand);
    }

    @Test
    void givenInvalidCreateProductRequest_whenCreateProduct_thenReturnBadRequest() throws Exception {
        // Given
        ProductCreateRequest mockCreateRequest = new ProductCreateRequest(
                null,
                null,
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        ProductCreateCommand productCreateCommand = productCreateRequestToCommandMapper.map(mockCreateRequest);

        Mockito.doThrow(new ProductNotFoundException()).when(productService)
                .createProduct(productCreateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenValidUpdateProductRequest_whenProductFound_thenReturnSuccess() throws Exception {
        //Given
        String productId = String.valueOf(UUID.randomUUID());

        // When
        ProductUpdateRequest mockUpdateRequest = new ProductUpdateRequest(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        ProductUpdateCommand updateCommand = productUpdateRequestToCommandMapper.map(mockUpdateRequest);

        Mockito.doNothing().when(productService).updateProduct(productId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockUpdateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(productService).updateProduct(productId, updateCommand);
    }

    @Test
    void givenInvalidUpdateProductRequest_whenProductNotExist_thenReturnBadRequest() throws Exception {
        // Given
        String productId = String.valueOf(UUID.randomUUID());
        ProductUpdateRequest mockUpdateRequest = new ProductUpdateRequest(
                null,
                "ingredients",
                null,
                ProductStatus.ACTIVE,
                null,
                ExtentType.GR
        );

        ProductUpdateCommand updateCommand = productUpdateRequestToCommandMapper.map(mockUpdateRequest);

        //When
        Mockito.doThrow(new ProductNotFoundException()).when(productService)
                .updateProduct(productId, updateCommand);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockUpdateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }


    @Test
    void givenUpdateProductIdEmpty_whenProductNotFound_thenException() throws Exception {
        // Given
        String productId = "";

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenInvalidUpdateRequest_whenProductIdNotFound_thenReturnBadRequest() throws Exception {
        //Given
        String productId = "invalidProductId";

        // When
        ProductUpdateRequest mockUpdateRequest = new ProductUpdateRequest(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        ProductUpdateCommand updateCommand = productUpdateRequestToCommandMapper.map(mockUpdateRequest);

        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .updateProduct(productId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }

    @Test
    void givenValidDeleteProductId_whenProductFound_thenReturnSuccess() throws Exception {
        // Given
        String productId = String.valueOf(UUID.randomUUID());

        // When
        Mockito.doNothing().when(productService).deleteProduct(productId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(productService).deleteProduct(productId);
    }

    @Test
    void givenInvalidDeleteProductId_whenProductNotExist_thenReturnNotFound() throws Exception {
        // Given
        String productId = "1L";

        // When
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .deleteProduct(productId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(productService).deleteProduct(productId);
    }

    @Test
    void givenProductIdEmpty_whenDeleteProductNotFound_thenException() throws Exception {
        // Given
        String productId = "";

        // When
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .deleteProduct(productId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", productId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verifyNoInteractions(productService);
    }
}