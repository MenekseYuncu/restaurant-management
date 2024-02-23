package org.violet.restaurantmanagement.product.category.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.Responses;
import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.category.model.mapper.CategoryToCategoryResponseMapper;
import org.violet.restaurantmanagement.product.category.service.CategoryService;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

@RestController
@RequestMapping("/api/v1/category")
class CategoryController {

    private final CategoryService categoryService;
    private final CategoryToCategoryResponseMapper toCategoryResponseMapper;

    CategoryController(CategoryService categoryService, CategoryToCategoryResponseMapper toCategoryResponseMapper) {
        this.categoryService = categoryService;
        this.toCategoryResponseMapper = toCategoryResponseMapper;
    }

    @GetMapping("/{id}")
    public Responses<CategoryResponse> getCategoryById(
            @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse categoryResponse = toCategoryResponseMapper.map(category);
        return Responses.successOf(categoryResponse);
    }

    @PostMapping
    public Responses<Void> createCategory(
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        categoryService.createCategory(request);
        return Responses.SUCCESS;
    }
}
