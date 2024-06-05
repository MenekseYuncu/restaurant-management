package org.violet.restaurantmanagement.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.category.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.exceptions.CategoryStatusAlreadyChangedException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.model.mapper.CategoryCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.category.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.category.model.mapper.CategoryToCategoryEntityMapper;
import org.violet.restaurantmanagement.category.model.mapper.CategoryUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.category.service.CategoryService;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.RmaPage;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;
    private static final CategoryToCategoryEntityMapper categoryToCategoryEntityMapper = CategoryToCategoryEntityMapper.INSTANCE;
    private static final CategoryCreateCommandToDomainMapper categoryCreateCommandToDomainMapper = CategoryCreateCommandToDomainMapper.INSTANCE;
    private static final CategoryUpdateCommandToDomainMapper categoryUpdateCommandToDomainMapper = CategoryUpdateCommandToDomainMapper.INSTANCE;


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

        return this.getById(id);
    }

    @Override
    public void createCategory(CategoryCreateCommand createCommand) {

        this.checkExistingOfCategoryName(createCommand.name());

        Category category = categoryCreateCommandToDomainMapper.map(createCommand);
        category.isActive();

        this.save(category);
    }

    @Override
    public void updateCategory(Long id, CategoryUpdateCommand updateCommand) {
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        this.checkExistingOfCategoryNameIfChanged(updateCommand, existingCategory);

        Category updatedCategory = categoryUpdateCommandToDomainMapper.map(updateCommand);

        this.checkExistingStatus(existingCategory.getStatus(), updatedCategory.getStatus());

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setStatus(updatedCategory.getStatus());

        categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        this.checkExistingStatus(categoryEntity.getStatus(), CategoryStatus.DELETED);

        categoryEntity.delete();

        categoryRepository.save(categoryEntity);
    }

    private Category getById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        return categoryEntityToDomainMapper.map(categoryEntity);
    }

    private void save(Category category) {
        CategoryEntity categoryEntityToBeSave = categoryToCategoryEntityMapper.map(category);
        categoryRepository.save(categoryEntityToBeSave);
    }

    private void checkExistingStatus(CategoryStatus currentStatus, CategoryStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new CategoryStatusAlreadyChangedException();
        }
    }

    private void checkExistingOfCategoryName(String name) {
        boolean isCategoryExistByName = categoryRepository.findByName(name).isPresent();
        if (isCategoryExistByName) {
            throw new CategoryAlreadyExistsException();
        }
    }

    private void checkExistingOfCategoryNameIfChanged(CategoryUpdateCommand categoryUpdateCommand,
                                                      CategoryEntity categoryEntity
    ) {
        if (!categoryEntity.getName().equalsIgnoreCase(categoryUpdateCommand.name())) {
            this.checkExistingOfCategoryName(categoryUpdateCommand.name());
        }
    }
}
