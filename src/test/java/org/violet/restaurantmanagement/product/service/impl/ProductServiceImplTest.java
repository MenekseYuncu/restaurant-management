package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.Sorting;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.model.mapper.ProductCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductDomainToProductEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductEntityToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;
import org.violet.restaurantmanagement.util.RmaServiceTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class ProductServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;
    private static final ProductCreateCommandToDomainMapper productCreateCommandToDomainMapper = ProductCreateCommandToDomainMapper.INSTANCE;
    private static final ProductUpdateCommandToDomainMapper productUpdateCommandToDomainMapper = ProductUpdateCommandToDomainMapper.INSTANCE;
    private static final ProductEntityToDomainMapper productEntityToDomainMapper = ProductEntityToDomainMapper.INSTANCE;


    @Test
    void givenGetAllProducts_whenProductListExist_thenReturnCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 1",
                "ingredient",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        ));
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 2",
                "ingredient",
                BigDecimal.valueOf(50),
                ProductStatus.ACTIVE,
                300,
                ExtentType.ML
        ));
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 3",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        ));
        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .sorting(Sorting.builder().direction(Sort.Direction.ASC).property("price").build())
                .filter(ProductListCommand.ProductFilter.builder()
                        .name("Product")
                        .categoryId(1L)
                        .statuses(Collections.singleton(ProductStatus.ACTIVE))
                        .build())
                .build();

        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        RmaPage<Product> result = productService.getAllProducts(givenProductListCommand);

        // Assertions
        Mockito.verify(productRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getName(), productEntities.get(0).getName());
        Assertions.assertEquals(result.getContent().get(1).getName(), productEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(2).getName(), productEntities.get(2).getName());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllProduct_whenProductListWithoutFilterAndSorting_thenReturnCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 1",
                "ingredient",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        ));
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 2",
                "ingredient",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.ML
        ));
        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .build();

        // When
        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        RmaPage<Product> result = productService.getAllProducts(givenProductListCommand);

        // Assertions
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllProduct_whenProductListWithoutSorting_thenReturnCategories() {
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                1L,
                "Product 1",
                "ingredient",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.ML
        ));

        productEntities.add(new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                2L,
                "Product 2",
                "ingredient",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.ML
        ));

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .filter(ProductListCommand.ProductFilter.builder()
                        .name("Product")
                        .statuses(Collections.singleton(ProductStatus.ACTIVE)).build())
                .build();

        // When
        Mockito.when(productRepository.findAll(
                Mockito.<Specification<ProductEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(productEntityPage);

        RmaPage<Product> result = productService.getAllProducts(givenProductListCommand);

        // Assertions
        Mockito.verify(productRepository, Mockito.never()).findAll();

        Assertions.assertEquals(productEntities.size(), result.getContent().size());
        Assertions.assertEquals(result.getContent().get(0).getName(), productEntities.get(0).getName());
        Assertions.assertEquals(result.getContent().get(1).getName(), productEntities.get(1).getName());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), productEntities.size());
        Assertions.assertEquals(productEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllProduct_whenProductListInvalidPriceRange_thenThrowIllegalArgumentException() {
        // Given
        ProductListCommand.ProductPriceRange invalidPriceRange = new ProductListCommand.ProductPriceRange(
                BigDecimal.valueOf(-100),
                BigDecimal.valueOf(200)
        );

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .filter(ProductListCommand.ProductFilter.builder()
                        .name("Product")
                        .statuses(Collections.singleton(ProductStatus.ACTIVE))
                        .priceRange(invalidPriceRange)
                        .build())
                .build();

        // When/Then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> productService.getAllProducts(givenProductListCommand)
        );
    }

    @Test
    void givenGetAllProducts_whenResultIsNull_thenThrowNullPointerException() {
        // When
        ProductListCommand productListCommand = ProductListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> productService.getAllProducts(productListCommand)
        );
    }

    @Test
    void givenGetProductById_whenProductExists_thenReturnProduct() {
        //Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductEntity productEntity = ProductEntity.builder()
                .id(productId)
                .categoryId(1L)
                .name("Product")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build();


        //When
        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.of(productEntity));

        Product mockProduct = productEntityToDomainMapper.map(productEntity);

        //Then
        Product resultProduct = productService.getProductById(productId);

        Assertions.assertNotNull(resultProduct);
        Assertions.assertEquals(mockProduct.getId(), resultProduct.getId());
        Assertions.assertEquals(mockProduct.getCategoryId(), resultProduct.getCategoryId());
        Assertions.assertEquals(mockProduct.getName(), resultProduct.getName());
        Assertions.assertEquals(mockProduct.getIngredient(), resultProduct.getIngredient());
        Assertions.assertEquals(mockProduct.getPrice(), resultProduct.getPrice());
        Assertions.assertEquals(mockProduct.getStatus(), resultProduct.getStatus());
        Assertions.assertEquals(mockProduct.getExtent(), resultProduct.getExtent());
        Assertions.assertEquals(mockProduct.getExtentType(), resultProduct.getExtentType());
        Assertions.assertEquals(mockProduct.getCreatedAt(), resultProduct.getCreatedAt());
    }

    @Test
    void givenGetProductById_whenProductDoesNotExist_thenThrowProductNotFoundException() {
        //Given
        String productId = "b5db-4b71-bdb0-8eed335fd6e4";

        //When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(productId));
    }

    @Test
    void givenEmptyProductId_whenGetProductById_thenThrowProductNotFoundException() {
        // Given
        String productId = "";

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(productId));
    }

    @Test
    void givenCreateProduct_whenSaveFails_thenThrowException() {
        // Given
        ProductCreateCommand createCommand = new ProductCreateCommand(
                null,
                "Product",
                "ingredients",
                null,
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(productRepository.save(any(ProductEntity.class)))
                .thenThrow(ProductNotFoundException.class);

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.createProduct(createCommand));

        Mockito.verify(productRepository, times(1))
                .save(any(ProductEntity.class));
    }

    @Test
    void givenCreateProduct_thenSaveProductEntity() {
        // Gİven
        ProductCreateCommand createCommand = new ProductCreateCommand(
                1L,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        Product product = productCreateCommandToDomainMapper.map(createCommand);
        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);

        // When
        Mockito.when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        productService.createProduct(createCommand);

        // Then
        Mockito.verify(productRepository, times(1))
                .save(any(ProductEntity.class));
    }

    @Test
    void givenUpdateProduct_whenProductExists_thenUpdateProductEntity() {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );
        Product product = productUpdateCommandToDomainMapper.map(updateCommand);

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);

        // When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(productEntity));

        productService.updateProduct(productId, updateCommand);

        // Then
        Mockito.verify(productRepository, times(1))
                .save(any(ProductEntity.class));
    }

    @Test
    void givenUpdateProduct_whenProductIdDoesNotExists_thenThrowProductNotFoundException() {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(productId, updateCommand));
    }

    @Test
    void givenUpdateProduct_whenNullUpdateCommand_thenThrowException() {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductUpdateCommand updateCommand = null;

        // Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(productId, updateCommand));

        Mockito.verify(productRepository, times(0)).save(any(ProductEntity.class));
    }

    @Test
    void givenSoftDeleteProduct_whenProductExists_thenUpdatedProductEntity() {
        // Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";
        ProductEntity productEntity = new ProductEntity();

        // When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        productService.deleteProduct(productId);

        // Then
        Mockito.verify(productRepository, times(1))
                .save(any(ProductEntity.class));
    }

    @Test
    void givenSoftDeleteProduct_whenProductIdDoesNotExists_thenThrowProductNotFoundException() {
        //Given
        String productId = "5f98b326-b5db-4b71-bdb0-8eed335fd6e4";

        //When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(productId));
    }
}