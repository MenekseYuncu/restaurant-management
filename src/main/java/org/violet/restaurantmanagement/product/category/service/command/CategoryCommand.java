package org.violet.restaurantmanagement.product.category.service.command;

import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;

public interface CategoryCommand {

    GeneralResponse execute(CategoryCreateRequest request);
}
