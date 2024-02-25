package org.violet.restaurantmanagement.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryToCategoryResponseMapper;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
class CategoryController {

    private final CategoryService categoryService;
    private static final CategoryToCategoryResponseMapper toCategoryResponseMapper = CategoryToCategoryResponseMapper.INSTANCE;
    private static final CategoryCreateRequestToCreateCommandMapper toCreateCommandMapper = CategoryCreateRequestToCreateCommandMapper.INSTANCE;


    @GetMapping("/{id}")
    public BaseResponse<CategoryResponse> getCategoryById(
            @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse categoryResponse = toCategoryResponseMapper.map(category);
        return BaseResponse.successOf(categoryResponse);
    }

    @PostMapping
    public BaseResponse<Void> createCategory(
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        CategoryCreateCommand command = toCreateCommandMapper.map(request);
        categoryService.createCategory(command);
        return BaseResponse.SUCCESS;
    }
}
