package org.violet.restaurantmanagement.product.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.common.pegable.PageContent;
import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.product.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryToCategoryResponseMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateRequestToUpdateCommandMapper;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private static final CategoryToCategoryResponseMapper toCategoryResponseMapper = CategoryToCategoryResponseMapper.INSTANCE;
    private static final CategoryCreateRequestToCreateCommandMapper toCreateCommandMapper = CategoryCreateRequestToCreateCommandMapper.INSTANCE;
    private static final CategoryUpdateRequestToUpdateCommandMapper toUpdateCommandMapper = CategoryUpdateRequestToUpdateCommandMapper.INSTANCE;

    @PostMapping("/categories")
    public BaseResponse<PageContent<Category>> getAllCategories(
            @Valid @RequestBody CategoryListCommand categoryListCommand) {
        PageContent<Category> pageContent = categoryService.getAllCategories(categoryListCommand);
        return BaseResponse.successOf(pageContent);
    }

    @GetMapping("/{id}")
    public BaseResponse<CategoryResponse> getCategoryById(
            @PathVariable @Positive Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse categoryResponse = toCategoryResponseMapper.map(category);
        return BaseResponse.successOf(categoryResponse);
    }

    @PostMapping
    public BaseResponse<Void> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryCreateCommand command = toCreateCommandMapper.map(request);
        categoryService.createCategory(command);
        return BaseResponse.SUCCESS;
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateCategory(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        CategoryUpdateCommand command = toUpdateCommandMapper.map(request);
        categoryService.updateCategory(id, command);
        return BaseResponse.SUCCESS;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deletedCategory(
            @PathVariable @Positive Long id
    ){
        categoryService.deleteCategory(id);
        return BaseResponse.SUCCESS;
    }
}
