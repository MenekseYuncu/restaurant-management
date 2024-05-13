package org.violet.restaurantmanagement.product.service;

import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;

public interface ProductService {

    void createProduct(ProductCreateCommand createCommand);

}
