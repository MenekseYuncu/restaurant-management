package org.violet.restaurantmanagement.product.category.service.command;

import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

public interface CategoryCommand {

    Category getCategoryById(Long id);

    GeneralResponse execute(CategoryCreateRequest request);

}
