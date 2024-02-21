package org.violet.restaurantmanagement.product.category.service.impl;

import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.category.service.CategoryService;
import org.violet.restaurantmanagement.product.category.service.command.CategoryCommand;

@Service
class CategoryServiceImpl implements CategoryService {

    private final CategoryCommand command;

    CategoryServiceImpl(CategoryCommand command) {
        this.command = command;
    }

    @Override
    public GeneralResponse createCategory(CategoryCreateRequest request) {
        return command.execute(request);
    }
}
