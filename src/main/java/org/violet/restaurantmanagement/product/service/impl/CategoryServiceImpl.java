package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.common.pegable.RmaPage;
import org.violet.restaurantmanagement.product.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;
    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;
    private static final CategoryUpdateCommandToEntityMapper categoryUpdateCommandToEntityMapper = CategoryUpdateCommandToEntityMapper.INSTANCE;


    @Override
    public RmaPage<Category> getAllCategories(CategoryListCommand categoryListCommand) {

        Page<CategoryEntity> categoryEntityPage = categoryRepository.findAll(
                categoryListCommand.toSpecification(CategoryEntity.class),
                categoryListCommand.toPageable()
        );

        return RmaPage.<Category>builder()
                .content(categoryEntityToDomainMapper.map(categoryEntityPage.getContent()))
                .page(categoryEntityPage)
                .sortedBy(categoryListCommand.getSorting())
                .filteredBy(categoryListCommand.getFilter())
                .build();
    }

    @Override
    public Category getCategoryById(Long id) {
        CategoryEntity entity = categoryRepository
                .findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        return categoryEntityToDomainMapper.map(entity);
    }

    @Override
    public void createCategory(CategoryCreateCommand createCommand) {
        CategoryEntity categoryEntity = categoryCreateCommandToEntityMapper.map(createCommand);

        if (categoryRepository.findByName(categoryEntity.getName())) {
            throw new CategoryAlreadyExistsException();
        }

        categoryRepository.save(categoryEntity);
    }

    @Override
    public void updateCategory(Long id, CategoryUpdateCommand updateCommand) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        CategoryEntity updatedEntity = categoryUpdateCommandToEntityMapper.map(updateCommand);

        entity.setName(updatedEntity.getName());

        if (categoryRepository.findByName(updatedEntity.getName())){
            throw new CategoryAlreadyExistsException();
        }

        entity.setStatus(updatedEntity.getStatus());

        categoryRepository.save(entity);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        categoryEntity.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(categoryEntity);
    }
}
