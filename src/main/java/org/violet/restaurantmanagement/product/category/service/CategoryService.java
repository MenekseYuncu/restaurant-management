package org.violet.restaurantmanagement.product.category.service;

import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;

public interface CategoryService {

    GeneralResponse createCategory(CategoryCreateRequest request);
}
