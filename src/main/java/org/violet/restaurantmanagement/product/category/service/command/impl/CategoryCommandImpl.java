package org.violet.restaurantmanagement.product.category.service.command.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.violet.restaurantmanagement.product.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.category.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.category.model.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.category.model.mapper.CategoryEntityToCategoryMapper;
import org.violet.restaurantmanagement.product.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.category.service.command.CategoryCommand;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CategoryCommandImpl implements CategoryCommand {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityToCategoryMapper categoryMapper;


    @Override
    public Category getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category does not exists"));

        return categoryMapper.mapToDto(categoryEntity);
    }

    @Override
    public GeneralResponse execute(CategoryCreateRequest request) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(request.name());
        categoryEntity.setStatus(request.status());
        categoryEntity.setCreatedAt(LocalDateTime.now());
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        Category mappedCategory = categoryMapper.mapToDto(savedCategory);

        return new GeneralResponse(
                LocalDateTime.now(),
                HttpStatus.OK.toString(),
                mappedCategory != null
        );
    }
}
