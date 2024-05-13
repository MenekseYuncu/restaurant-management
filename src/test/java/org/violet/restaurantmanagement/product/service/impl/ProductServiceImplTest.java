package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.model.mapper.ProductCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductDomainToProductEntityMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;
import org.violet.restaurantmanagement.util.RmaServiceTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class ProductServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;
    private static final ProductCreateCommandToDomainMapper productCreateCommandToDomainMapper = ProductCreateCommandToDomainMapper.INSTANCE;


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
}