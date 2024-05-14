package org.violet.restaurantmanagement.product.service;

import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

public interface ProductService {

    RmaPage<Product> getAllProducts(ProductListCommand productListCommand);

    Product getProductById(String id);

    void createProduct(ProductCreateCommand createCommand);

    void updateProduct(String id, ProductUpdateCommand updateCommand);

    void deleteProduct(String id);
}
