package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;


@Service
@RequiredArgsConstructor
class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;
    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;


    @Override
    public Category getCategoryById(Long id) {
        CategoryEntity entity = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category does not exists"));
        return categoryEntityToDomainMapper.map(entity);
    }

    @Override
    public void createCategory(CategoryCreateCommand createCommand) {
        CategoryEntity categoryEntity = categoryCreateCommandToEntityMapper.map(createCommand);
        categoryRepository.save(categoryEntity);
    }
}
