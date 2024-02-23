package org.violet.restaurantmanagement.product.service.command.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.controller.response.GeneralResponse;
import org.violet.restaurantmanagement.product.model.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.model.mapper.CategoryEntityToCategoryMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.service.command.CategoryCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

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
    public void execute(CategoryCreateRequest request) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(request.name());
        categoryEntity.setStatus(request.status());
        categoryEntity.setCreatedAt(LocalDateTime.now());
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        Category mappedCategory = categoryMapper.mapToDto(savedCategory);

        new GeneralResponse(
                LocalDateTime.now(),
                HttpStatus.OK.toString(),
                mappedCategory != null
        );
    }
}
