package org.violet.restaurantmanagement.category.controller;

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
import org.violet.restaurantmanagement.category.controller.mapper.CategoryCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.category.controller.mapper.CategoryListRequestToListCommandMapper;
import org.violet.restaurantmanagement.category.controller.mapper.CategoryUpdateRequestToUpdateCommandMapper;
import org.violet.restaurantmanagement.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.category.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.category.model.mapper.CategoryToCategoryResponseMapper;
import org.violet.restaurantmanagement.category.service.CategoryService;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.common.controller.response.RmaPageResponse;
import org.violet.restaurantmanagement.common.model.RmaPage;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    private static final CategoryListRequestToListCommandMapper toCategoryListCommandMapper = CategoryListRequestToListCommandMapper.INSTANCE;
    private static final CategoryToCategoryResponseMapper toCategoryResponseMapper = CategoryToCategoryResponseMapper.INSTANCE;
    private static final CategoryCreateRequestToCommandMapper toCreateCommandMapper = CategoryCreateRequestToCommandMapper.INSTANCE;
    private static final CategoryUpdateRequestToUpdateCommandMapper toUpdateCommandMapper = CategoryUpdateRequestToUpdateCommandMapper.INSTANCE;


    @PostMapping("/categories")
    public BaseResponse<RmaPageResponse<CategoryResponse>> getAllCategories(
            @Valid @RequestBody CategoryListRequest categoryListRequest
    ) {

        RmaPage<Category> categoryPage = categoryService.getAllCategories(
                toCategoryListCommandMapper.map(categoryListRequest)
        );

        RmaPageResponse<CategoryResponse> categoryResponsePage = RmaPageResponse.<CategoryResponse>builder()
                .content(toCategoryResponseMapper.map(categoryPage.getContent()))
                .page(categoryPage)
                .build();
        return BaseResponse.successOf(categoryResponsePage);
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
