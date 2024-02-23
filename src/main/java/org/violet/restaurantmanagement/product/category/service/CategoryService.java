package org.violet.restaurantmanagement.product.category.service;

import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

public interface CategoryService {

    Category getCategoryById(Long id);

    GeneralResponse createCategory(CategoryCreateRequest request);
}
