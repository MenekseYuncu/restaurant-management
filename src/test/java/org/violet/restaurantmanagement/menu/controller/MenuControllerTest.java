package org.violet.restaurantmanagement.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.menu.controller.request.MenuListRequest;
import org.violet.restaurantmanagement.menu.service.MenuService;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = MenuController.class)
class MenuControllerTest extends RmaControllerTest {

    @MockBean
    private MenuService menuService;

    private final static String BASE_URL = "/api/v1/menu";


    @Test
    void givenValidMenuListRequest_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest.MenuFilter mockFilter = MenuListRequest.MenuFilter.builder()
                .name("product")
                .build();
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
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
                .price(BigDecimal.valueOf(50))
                .currency("TRY")
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .price(BigDecimal.valueOf(150))
                .currency("TRY")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .price(BigDecimal.valueOf(100))
                .currency("TRY")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockMenuListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockMenuListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .sortedBy(mockMenuListRequest.getSorting())
                .filteredBy(mockFilter)
                .build();

        Mockito.when(menuService.getAllMenu(
                Mockito.any(MenuListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
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

        // Verify
        Mockito.verify(menuService, Mockito.times(1))
                .getAllMenu(Mockito.any(MenuListCommand.class));
    }

    @Test
    void givenValidMenuListRequestWithoutSorting_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest.MenuFilter mockFilter = MenuListRequest.MenuFilter.builder()
                .name("product")
                .build();
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
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
                .price(BigDecimal.valueOf(50))
                .currency("TRY")
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .price(BigDecimal.valueOf(150))
                .currency("TRY")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .price(BigDecimal.valueOf(100))
                .currency("TRY")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockMenuListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockMenuListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .filteredBy(mockFilter)
                .build();

        Mockito.when(menuService.getAllMenu(
                Mockito.any(MenuListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
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

        // Verify
        Mockito.verify(menuService, Mockito.times(1))
                .getAllMenu(Mockito.any(MenuListCommand.class));
    }

    @Test
    void givenValidMenuListRequestWithoutFilter_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(3)
                        .pageNumber(1)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
                        .build()
                )
                .build();

        // When
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 1")
                .status(ProductStatus.ACTIVE)
                .price(BigDecimal.valueOf(50))
                .currency("TRY")
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .price(BigDecimal.valueOf(150))
                .currency("TRY")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .price(BigDecimal.valueOf(100))
                .currency("TRY")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockMenuListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockMenuListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .sortedBy(mockMenuListRequest.getSorting())
                .build();

        Mockito.when(menuService.getAllMenu(
                Mockito.any(MenuListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
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

        // Verify
        Mockito.verify(menuService, Mockito.times(1))
                .getAllMenu(Mockito.any(MenuListCommand.class));
    }

    @Test
    void givenValidMenuListRequestWithoutFilterAndSorting_whenProductsAndCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
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
                .price(BigDecimal.valueOf(50))
                .currency("TRY")
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 2")
                .price(BigDecimal.valueOf(150))
                .currency("TRY")
                .status(ProductStatus.ACTIVE)
                .build()
        );
        mockProducts.add(Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("product 3")
                .price(BigDecimal.valueOf(100))
                .currency("TRY")
                .status(ProductStatus.INACTIVE)
                .build()
        );

        RmaPage<Product> rmaPage = RmaPage.<Product>builder()
                .content(mockProducts)
                .pageNumber(mockMenuListRequest.getPagination().getPageNumber())
                .pageSize(mockProducts.size())
                .totalPageCount(mockMenuListRequest.getPagination().getPageNumber())
                .totalElementCount(mockProducts.size())
                .build();

        Mockito.when(menuService.getAllMenu(
                Mockito.any(MenuListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
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

        // Verify
        Mockito.verify(menuService, Mockito.times(1))
                .getAllMenu(Mockito.any(MenuListCommand.class));
    }

    @Test
    void givenInvalidInput_whenGetAllMenu_thenReturnBadRequest() throws Exception {
        // Given
        MenuListRequest menuListRequest = MenuListRequest.builder().build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(menuService);
    }

    @Test
    void givenInvalidNegativePageSize_whenGetAllMenu_thenReturnBadRequest() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(-1)
                        .pageNumber(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(menuService);
    }

    @Test
    void givenInvalidNegativePageNumber_whenGetAllMenu_thenReturnBadRequest() throws Exception {
        // Given
        MenuListRequest mockMenuListRequest = MenuListRequest.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(-1)
                        .pageSize(1)
                        .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockMenuListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(menuService);
    }
}