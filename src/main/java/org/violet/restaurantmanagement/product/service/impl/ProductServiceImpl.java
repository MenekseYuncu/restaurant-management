package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.product.exceptions.ProductAlreadyExistException;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.exceptions.ProductStatusAlreadyChanged;
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
        this.checkExistingOfCategory(createCommand.categoryId());

        Product product = productCreateCommandToDomainMapper.map(createCommand);
        productRepository.save(productDomainToProductEntityMapper.map(product));
    }

    @Override
    public void updateProduct(String id, ProductUpdateCommand updateCommand) {
        this.checkExistingOfCategory(updateCommand.categoryId());

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        this.checkExistingOfProductNameIfChanged(updateCommand, existingProduct);

        Product updatedProduct = productUpdateCommandToDomainMapper.map(updateCommand);
        this.checkExistingStatus(existingProduct.getStatus(), updatedProduct.getStatus());

        existingProduct.setCategoryId(updatedProduct.getCategoryId());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setIngredient(updatedProduct.getIngredient());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setExtent(updatedProduct.getExtent());
        existingProduct.setExtentType(updatedProduct.getExtentType());

        productRepository.save(existingProduct);
    }

    private void checkExistingOfCategory(Long categoryId) {
        boolean isCategoryNotPresent = !categoryRepository
                .existsByIdAndStatusIsNot(categoryId, CategoryStatus.DELETED);
        if (isCategoryNotPresent) {
            throw new CategoryNotFoundException();
        }
    }

    private void checkExistingOfProductNameIfChanged(ProductUpdateCommand productUpdateCommand,
                                                     ProductEntity productEntity) {
        if (!productEntity.getName().equalsIgnoreCase(productUpdateCommand.name())) {
            boolean isProductExistByName = productRepository.findByName(productUpdateCommand.name()).isPresent();
            if (isProductExistByName) {
                throw new ProductAlreadyExistException();
            }
        }
    }

    @Override
    public void deleteProduct(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        this.checkExistingStatus(productEntity.getStatus(), ProductStatus.DELETED);
        productEntity.delete();

        productRepository.save(productEntity);
    }

    private void checkExistingStatus(ProductStatus currentStatus, ProductStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new ProductStatusAlreadyChanged();
        }
    }
}
