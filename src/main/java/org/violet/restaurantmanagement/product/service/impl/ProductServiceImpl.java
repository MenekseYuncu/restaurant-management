package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.product.model.mapper.ProductCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductDomainToProductEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private static final ProductDomainToProductEntityMapper productDomainToProductEntityMapper = ProductDomainToProductEntityMapper.INSTANCE;
    private static final ProductCreateCommandToDomainMapper productCreateCommandToDomainMapper = ProductCreateCommandToDomainMapper.INSTANCE;
    private static final ProductUpdateCommandToDomainMapper productUpdateCommandToDomainMapper = ProductUpdateCommandToDomainMapper.INSTANCE;

    @Override
    public void createProduct(ProductCreateCommand createCommand) {
        Product product = productCreateCommandToDomainMapper.map(createCommand);

        ProductEntity productEntity = productDomainToProductEntityMapper.map(product);

        productRepository.save(productEntity);
    }

    @Override
    public void updateProduct(String id, ProductUpdateCommand updateCommand) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        Product updatedProduct = productUpdateCommandToDomainMapper.map(updateCommand);

        productEntity.setName(updatedProduct.getName());
        productEntity.setIngredient(updatedProduct.getIngredient());
        productEntity.setPrice(updatedProduct.getPrice());
        productEntity.setStatus(updatedProduct.getStatus());
        productEntity.setExtent(updatedProduct.getExtent());
        productEntity.setExtentType(updatedProduct.getExtentType());

        productRepository.save(productEntity);
    }
}
