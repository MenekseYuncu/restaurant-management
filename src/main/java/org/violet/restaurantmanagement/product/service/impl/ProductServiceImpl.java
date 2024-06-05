package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
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
    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;


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
        return this.getById(id);
    }

    @Override
    public void createProduct(ProductCreateCommand createCommand) {
        this.checkExistingOfCategory(createCommand.categoryId());

        Product product = productCreateCommandToDomainMapper.map(createCommand);

        this.save(product);
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

    @Override
    public void deleteProduct(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        this.checkExistingStatus(productEntity.getStatus(), ProductStatus.DELETED);
        productEntity.delete();

        productRepository.save(productEntity);
    }


    private Product getById(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        Product product = productEntityToDomainMapper.map(productEntity);
        this.getCategory(product);

        return product;
    }

    private void getCategory(Product product) {
        CategoryEntity categoryEntity = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        product.setCategory(categoryEntityToDomainMapper.map(categoryEntity));
    }

    private void save(Product product) {
        ProductEntity productEntityToBeSave = productDomainToProductEntityMapper.map(product);
        productRepository.save(productEntityToBeSave);
    }

    private void checkExistingStatus(ProductStatus currentStatus, ProductStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new ProductStatusAlreadyChanged();
        }
    }

    private void checkExistingOfProductName(String name) {
        boolean isProductExistByName = productRepository.findByName(name).isPresent();
        if (isProductExistByName) {
            throw new ProductAlreadyExistException();
        }
    }

    private void checkExistingOfProductNameIfChanged(ProductUpdateCommand productUpdateCommand,
                                                     ProductEntity productEntity
    ) {
        if (!productEntity.getName().equalsIgnoreCase(productUpdateCommand.name())) {
            this.checkExistingOfProductName(productUpdateCommand.name());
        }
    }

    private void checkExistingOfCategory(Long categoryId) {
        boolean isCategoryNotPresent = !categoryRepository
                .existsByIdAndStatusIsNot(categoryId, CategoryStatus.DELETED);
        if (isCategoryNotPresent) {
            throw new CategoryNotFoundException();
        }
    }
}
