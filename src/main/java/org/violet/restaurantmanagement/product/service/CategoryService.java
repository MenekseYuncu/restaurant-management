package org.violet.restaurantmanagement.product.service;

import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

public interface CategoryService {

    Category getCategoryById(Long id);

    void createCategory(CategoryCreateCommand command);
}
