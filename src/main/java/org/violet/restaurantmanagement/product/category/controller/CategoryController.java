package org.violet.restaurantmanagement.product.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.category.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createCategory(
            @RequestBody CategoryCreateRequest request
    ) {
        return new ResponseEntity<>(
                categoryService.createCategory(request),
                HttpStatus.OK
        );
    }
}
