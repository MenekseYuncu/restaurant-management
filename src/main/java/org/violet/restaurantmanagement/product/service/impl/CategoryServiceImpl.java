package org.violet.restaurantmanagement.product.service.impl;

import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;


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
