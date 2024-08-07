package org.violet.restaurantmanagement.menu.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.menu.service.domain.Menu;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class MenuServiceImplTest extends RmaServiceTest implements RmaTestContainer {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuServiceImpl menuService;


    @Test
    void givenMenuListExist_whenGetAllMenu_thenReturnProductsAndCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 1")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 2")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(50))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.ML)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(2L)
                .name("Product 3")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        MenuListCommand givenMenuListCommand = MenuListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build())
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
                        .build())
                .filter(MenuListCommand.MenuFilter.builder()
                        .name("product")
                        .build())
                .build();

        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        // When
        RmaPage<Menu> result = menuService.getAllMenu(givenMenuListCommand);

        // Verify
        Mockito.verify(productRepository, Mockito.times(1)).findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class));

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getName(), productEntities.getFirst().getName());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getIngredient(), productEntities.getFirst().getIngredient());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getStatus(), productEntities.getFirst().getStatus());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtent(), productEntities.get(0).getExtent());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtentType(), productEntities.get(0).getExtentType());

        Assertions.assertEquals(result.getContent().get(1).getProduct().getName(), productEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getIngredient(), productEntities.get(1).getIngredient());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getStatus(), productEntities.get(1).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtent(), productEntities.get(1).getExtent());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtentType(), productEntities.get(1).getExtentType());

        Assertions.assertEquals(result.getContent().get(2).getProduct().getName(), productEntities.get(2).getName());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getIngredient(), productEntities.get(2).getIngredient());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getStatus(), productEntities.get(2).getStatus());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtent(), productEntities.get(2).getExtent());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtentType(), productEntities.get(2).getExtentType());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenMenuListWithoutFilter_whenGetAllMenu_thenReturnProductsAndCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 1")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 2")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(50))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.ML)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(2L)
                .name("Product 3")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        MenuListCommand givenMenuListCommand = MenuListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build())
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("categoryId")
                        .build())
                .build();

        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        // When
        RmaPage<Menu> result = menuService.getAllMenu(givenMenuListCommand);

        // Verify
        Mockito.verify(productRepository, Mockito.times(1)).findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class));

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getName(), productEntities.getFirst().getName());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getIngredient(), productEntities.getFirst().getIngredient());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getStatus(), productEntities.getFirst().getStatus());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtent(), productEntities.get(0).getExtent());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtentType(), productEntities.get(0).getExtentType());

        Assertions.assertEquals(result.getContent().get(1).getProduct().getName(), productEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getIngredient(), productEntities.get(1).getIngredient());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getStatus(), productEntities.get(1).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtent(), productEntities.get(1).getExtent());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtentType(), productEntities.get(1).getExtentType());

        Assertions.assertEquals(result.getContent().get(2).getProduct().getName(), productEntities.get(2).getName());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getIngredient(), productEntities.get(2).getIngredient());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getStatus(), productEntities.get(2).getStatus());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtent(), productEntities.get(2).getExtent());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtentType(), productEntities.get(2).getExtentType());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenMenuListWithoutSorting_whenGetAllMenu_thenReturnProductsAndCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 1")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 2")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(50))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.ML)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(2L)
                .name("Product 3")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        MenuListCommand givenMenuListCommand = MenuListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build())
                .filter(MenuListCommand.MenuFilter.builder()
                        .name("product")
                        .build())
                .build();

        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        // When
        RmaPage<Menu> result = menuService.getAllMenu(givenMenuListCommand);

        // Verify
        Mockito.verify(productRepository, Mockito.times(1)).findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class));

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getName(), productEntities.getFirst().getName());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getIngredient(), productEntities.getFirst().getIngredient());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getStatus(), productEntities.getFirst().getStatus());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtent(), productEntities.get(0).getExtent());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtentType(), productEntities.get(0).getExtentType());

        Assertions.assertEquals(result.getContent().get(1).getProduct().getName(), productEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getIngredient(), productEntities.get(1).getIngredient());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getStatus(), productEntities.get(1).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtent(), productEntities.get(1).getExtent());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtentType(), productEntities.get(1).getExtentType());

        Assertions.assertEquals(result.getContent().get(2).getProduct().getName(), productEntities.get(2).getName());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getIngredient(), productEntities.get(2).getIngredient());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getStatus(), productEntities.get(2).getStatus());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtent(), productEntities.get(2).getExtent());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtentType(), productEntities.get(2).getExtentType());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenMenuListWithoutSortingAndFilter_whenGetAllMenu_thenReturnProductsAndCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 1")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product 2")
                .ingredient("ingredient")
                .price(BigDecimal.valueOf(50))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.ML)
                .build());
        productEntities.add(ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(2L)
                .name("Product 3")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build());

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        MenuListCommand givenMenuListCommand = MenuListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build())
                .build();

        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        // When
        RmaPage<Menu> result = menuService.getAllMenu(givenMenuListCommand);

        // Verify
        Mockito.verify(productRepository, Mockito.times(1)).findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class));

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getName(), productEntities.getFirst().getName());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getIngredient(), productEntities.getFirst().getIngredient());
        Assertions.assertEquals(result.getContent().getFirst().getProduct().getStatus(), productEntities.getFirst().getStatus());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtent(), productEntities.get(0).getExtent());
        Assertions.assertEquals(result.getContent().get(0).getProduct().getExtentType(), productEntities.get(0).getExtentType());

        Assertions.assertEquals(result.getContent().get(1).getProduct().getName(), productEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getIngredient(), productEntities.get(1).getIngredient());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getStatus(), productEntities.get(1).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtent(), productEntities.get(1).getExtent());
        Assertions.assertEquals(result.getContent().get(1).getProduct().getExtentType(), productEntities.get(1).getExtentType());

        Assertions.assertEquals(result.getContent().get(2).getProduct().getName(), productEntities.get(2).getName());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getIngredient(), productEntities.get(2).getIngredient());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getStatus(), productEntities.get(2).getStatus());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtent(), productEntities.get(2).getExtent());
        Assertions.assertEquals(result.getContent().get(2).getProduct().getExtentType(), productEntities.get(2).getExtentType());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenResultIsNull_whenGetAllProducts_thenThrowNullPointerException() {
        // When
        MenuListCommand menuListCommand = MenuListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> menuService.getAllMenu(menuListCommand)
        );
    }

}