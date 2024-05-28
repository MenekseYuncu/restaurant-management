package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.product.exceptions.ProductAlreadyExistException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProductServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;
    private static final ProductCreateCommandToDomainMapper productCreateCommandToDomainMapper = ProductCreateCommandToDomainMapper.INSTANCE;
    private static final ProductUpdateCommandToDomainMapper productUpdateCommandToDomainMapper = ProductUpdateCommandToDomainMapper.INSTANCE;
    private static final ProductEntityToDomainMapper productEntityToDomainMapper = ProductEntityToDomainMapper.INSTANCE;


    @Test
    void givenProductListExist_whenGetAllProducts_thenReturnCategories() {
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
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("price")
                        .build()
                )
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
    void givenProductListWithoutFilter_whenGetAllProducts_thenReturnCategories() {
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
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(3)
                        .build()
                )
                .sorting(SortingBuilder.builder()
                        .asc()
                        .property("price")
                        .build()
                )
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
    void givenProductListWithoutFilterAndSorting_whenGetAllProduct_thenReturnCategories() {
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

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities);

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageSize(1)
                        .pageNumber(1)
                        .build()
                )
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
    void givenProductListWithoutSorting_whenGetAllProduct_thenReturnCategories() {
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
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(2)
                        .build()
                )
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
    void givenProductListInvalidPriceRange_whenGetAllProduct_thenThrowIllegalArgumentException() {
        // Given
        ProductListCommand.ProductPriceRange invalidPriceRange = new ProductListCommand.ProductPriceRange(
                BigDecimal.valueOf(-100),
                BigDecimal.valueOf(200)
        );

        ProductListCommand givenProductListCommand = ProductListCommand.builder()
                .pagination(PaginationBuilder
                        .builder()
                        .pageNumber(1)
                        .pageSize(1)
                        .build()
                )
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
    void givenResultIsNull_whenGetAllProducts_thenThrowNullPointerException() {
        // When
        ProductListCommand productListCommand = ProductListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> productService.getAllProducts(productListCommand)
        );
    }

    @Test
    void givenProductExists_whenGetProductById_thenReturnProduct() {
        //Given
        ProductEntity productEntity = ProductEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .categoryId(1L)
                .name("Product")
                .ingredient("ingredients")
                .price(BigDecimal.valueOf(100))
                .status(ProductStatus.ACTIVE)
                .extent(300)
                .extentType(ExtentType.GR)
                .build();

        //When
        Mockito.when(productRepository.findById(productEntity.getId()))
                .thenReturn(Optional.of(productEntity));

        Product mockProduct = productEntityToDomainMapper.map(productEntity);

        //Then
        Product resultProduct = productService.getProductById(productEntity.getId());

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
    void givenProductDoesNotExist_whenGetProductById_thenThrowProductNotFoundException() {
        //Given
        String productId = String.valueOf(UUID.randomUUID());

        //When
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

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
    void givenValidCreateProductCommand_whenCreateProductCorrect_thenSaveProductEntity() {
        // Gİven
        CategoryEntity categoryEntity = new CategoryEntity(
                1L,
                "Category",
                CategoryStatus.ACTIVE
        );
        ProductCreateCommand createCommand = new ProductCreateCommand(
                categoryEntity.getId(),
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(categoryEntity.getId(), CategoryStatus.DELETED))
                .thenReturn(true);

        Product product = productCreateCommandToDomainMapper.map(createCommand);
        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);

        Mockito.when(productRepository.save(ArgumentMatchers.any(ProductEntity.class)))
                .thenReturn(productEntity);

        productService.createProduct(createCommand);

        // Then
        Mockito.verify(productRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(ProductEntity.class));
    }

    @Test
    void givenInvalidCreateProductCommandCategoryId_whenCreateProduct_thenThrowException() {
        // Given
        Long categoryId = null;
        ProductCreateCommand createCommand = new ProductCreateCommand(
                categoryId,
                "Product Name",
                "ingredient",
                BigDecimal.valueOf(11),
                ProductStatus.ACTIVE,
                100,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(categoryId, CategoryStatus.DELETED))
                .thenReturn(false);

        // Assert
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> productService.createProduct(createCommand));

        // Verify
        Mockito.verify(productRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void givenInvalidCreateProductCommand_whenCreateProduct_thenThrowException() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity(
                1L,
                "Category",
                CategoryStatus.ACTIVE
        );
        ProductCreateCommand createCommand = new ProductCreateCommand(

                categoryEntity.getId(),
                null,
                "ingredients",
                null,
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // Assert
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> productService.createProduct(createCommand));

        Mockito.verify(productRepository, Mockito.never())
                .save(ArgumentMatchers.any(ProductEntity.class));
    }

    @Test
    void givenValidUpdateProductCommand_whenProductAndCategoryExists_thenUpdateProductEntity() {
        // Given
        Long categoryId = 3L;
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                "Category rename",
                CategoryStatus.ACTIVE
        );
        categoryRepository.save(categoryEntity);

        ProductEntity productEntity = new ProductEntity(
                String.valueOf(UUID.randomUUID()),
                categoryEntity.getId(),
                "Test products",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                100,
                ExtentType.GR
        );

        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                categoryEntity.getId(),
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(categoryEntity.getId(), CategoryStatus.DELETED))
                .thenReturn(true);


        Mockito.when(productRepository.findById(productEntity.getId()))
                .thenReturn(Optional.of(productEntity));


        Product product = productUpdateCommandToDomainMapper.map(updateCommand);
        ProductEntity entity = productDomainToProductEntityMapper.map(product);

        Mockito.when(productRepository.save(ArgumentMatchers.any(ProductEntity.class)))
                .thenReturn(entity);

        // Then
        Assertions.assertDoesNotThrow(
                () -> productService.updateProduct(productEntity.getId(), updateCommand));

        // Verify
        Mockito.verify(productRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(ProductEntity.class));
    }

    @Test
    void givenExistingProductName_whenUpdateProduct_thenThrowProductAlreadyExistException() {
        // Given
        Long categoryId = 1L;
        String existingProductName = "ExistingProduct";

        Product existingProduct = Product.builder()
                .categoryId(categoryId)
                .name("OriginalProduct")
                .ingredient("ingredients")
                .status(ProductStatus.ACTIVE)
                .price(BigDecimal.valueOf(100))
                .extent(100)
                .extentType(ExtentType.GR)
                .createdAt(LocalDateTime.now())
                .build();

        ProductEntity existingProductEntity = productDomainToProductEntityMapper.map(existingProduct);
        productRepository.save(existingProductEntity);

        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                categoryId,
                existingProductName,
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(categoryId, CategoryStatus.DELETED))
                .thenReturn(true);

        Mockito.when(productRepository.findById(existingProductEntity.getId()))
                .thenReturn(Optional.of(existingProductEntity));

        Mockito.when(productRepository.findByName(existingProductName))
                .thenReturn(Optional.of(existingProductEntity));

        // Then
        Assertions.assertThrows(ProductAlreadyExistException.class,
                () -> productService.updateProduct(existingProductEntity.getId(), updateCommand)
        );
    }

    @Test
    void givenInvalidCategoryId_whenUpdateProduct_thenThrowCategoryNotFoundException() {
        // Given
        Long invalidCategoryId = 999L;
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                invalidCategoryId,
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(invalidCategoryId, CategoryStatus.DELETED))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> productService.updateProduct("validProductId", updateCommand));
    }

    @Test
    void givenProductIdDoesNotExists_whenUpdateProduct_thenThrowProductNotFoundException() {
        // Given
        String productId = UUID.randomUUID().toString();
        Long categoryId = 4L;
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryId,
                "Category rename",
                CategoryStatus.ACTIVE
        );
        categoryRepository.save(categoryEntity);

        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                categoryEntity.getId(),
                "Product",
                "ingredients",
                BigDecimal.valueOf(100),
                ProductStatus.ACTIVE,
                300,
                ExtentType.GR
        );

        // When
        Mockito.when(categoryRepository.existsByIdAndStatusIsNot(categoryEntity.getId(), CategoryStatus.DELETED))
                .thenReturn(true);

        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(productId, updateCommand));
    }

    @Test
    void givenSoftDeleteProduct_whenProductExists_thenUpdatedProductEntity() {
        // Given
        String productId = UUID.randomUUID().toString();
        ProductEntity productEntity = new ProductEntity();

        // When
        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.of(productEntity));

        productService.deleteProduct(productId);

        // Then
        Mockito.verify(productRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(ProductEntity.class));
    }

    @Test
    void givenProductIdDoesNotExists_whenSoftDeleteProduct_thenThrowProductNotFoundException() {
        //Given
        String productId = UUID.randomUUID().toString();

        //When
        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(productId));
    }
}