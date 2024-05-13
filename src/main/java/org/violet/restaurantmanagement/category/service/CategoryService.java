package org.violet.restaurantmanagement.category.service;

import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;

public interface CategoryService {

    RmaPage<Category> getAllCategories(CategoryListCommand categoryListCommand);

    Category getCategoryById(Long id);

    void createCategory(CategoryCreateCommand command);

    void updateCategory(Long id, CategoryUpdateCommand updateCommand);

    void deleteCategory(Long id);
}
