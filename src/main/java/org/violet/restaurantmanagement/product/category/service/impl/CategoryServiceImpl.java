package org.violet.restaurantmanagement.product.category.service.impl;

import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.service.CategoryService;
import org.violet.restaurantmanagement.product.category.service.command.CategoryCommand;
import org.violet.restaurantmanagement.product.category.service.domain.Category;


@Service
class CategoryServiceImpl implements CategoryService {

    private final CategoryCommand command;

    CategoryServiceImpl(CategoryCommand command) {
        this.command = command;
    }

    @Override
    public Category getCategoryById(Long id) {
        return command.getCategoryById(id);
    }

    @Override
    public void createCategory(CategoryCreateRequest request) {
        command.execute(request);
    }
}
