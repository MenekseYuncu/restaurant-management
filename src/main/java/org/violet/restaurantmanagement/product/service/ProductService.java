package org.violet.restaurantmanagement.product.service;

import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;

public interface ProductService {

    void createProduct(ProductCreateCommand createCommand);

    void updateProduct(String id, ProductUpdateCommand updateCommand);

    void deleteProduct(String id);
}
