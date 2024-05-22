package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;
import org.violet.restaurantmanagement.product.model.mapper.ProductCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductDomainToProductEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductEntityToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;
    private static final ProductCreateCommandToDomainMapper productCreateCommandToDomainMapper = ProductCreateCommandToDomainMapper.INSTANCE;
    private static final ProductUpdateCommandToDomainMapper productUpdateCommandToDomainMapper = ProductUpdateCommandToDomainMapper.INSTANCE;
    private static final ProductEntityToDomainMapper productEntityToDomainMapper = ProductEntityToDomainMapper.INSTANCE;


    @Override
    public RmaPage<Product> getAllProducts(ProductListCommand productListCommand) {

        Page<ProductEntity> productEntityPage = productRepository.findAll(
                productListCommand.toSpecification(ProductEntity.class),
                productListCommand.toPageable()
        );

        return RmaPage.<Product>builder()
                .content(productEntityToDomainMapper.map(productEntityPage.getContent()))
                .page(productEntityPage)
                .sortedBy(productListCommand.getSorting())
                .filteredBy(productListCommand.getFilter())
                .build();
    }

    @Override
    public Product getProductById(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return productEntityToDomainMapper.map(productEntity);
    }

    @Override
    public void createProduct(ProductCreateCommand createCommand) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(createCommand.categoryId())
                .filter(CategoryEntity::isActive);

        if (categoryEntity.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        Product product = productCreateCommandToDomainMapper.map(createCommand);
        product.isActive();

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);

        productRepository.save(productEntity);
    }

    @Override
    public void updateProduct(String id, ProductUpdateCommand updateCommand) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(updateCommand.categoryId())
                .filter(CategoryEntity::isActive);

        if (categoryEntity.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        Product updatedProduct = productUpdateCommandToDomainMapper.map(updateCommand);

        productDomainToProductEntityMapper.map(updatedProduct);

        productRepository.save(productEntity);
    }

    @Override
    public void deleteProduct(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productEntity.setStatus(ProductStatus.DELETED);
        productRepository.save(productEntity);
    }
}
