package org.violet.restaurantmanagement.product.service.command;

import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.service.domain.Category;

public interface CategoryCommand {

    Category getCategoryById(Long id);

    void execute(CategoryCreateRequest request);

}
