package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.violet.restaurantmanagement.product.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;

    private static final CategoryUpdateCommandToEntityMapper categoryUpdateCommandToEntityMapper = CategoryUpdateCommandToEntityMapper.INSTANCE;

    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;


    @Test
    void givenGetCategoryById_whenCategoryExists_thenReturnCategory() {
        //Given
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setName("TestCategory");
        categoryEntity.setStatus(CategoryStatus.ACTIVE);

        //When
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryEntity));

        Category mockCategory = categoryEntityToDomainMapper.map(categoryEntity);

        //Then
        Category resultCategory = categoryService.getCategoryById(categoryId);

        Assertions.assertNotNull(resultCategory);
        Assertions.assertEquals(mockCategory.getId(), resultCategory.getId());
        Assertions.assertEquals(mockCategory.getName(), resultCategory.getName());
        Assertions.assertEquals(mockCategory.getStatus(), resultCategory.getStatus());
        Assertions.assertEquals(mockCategory.getCreatedAt(), resultCategory.getCreatedAt());
    }

    @Test
    void givenGetCategoryById_whenCategoryDoesNotExist_thenThrowCategoryNotFoundException() {
        //Given
        Long categoryId = 1L;

        //When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void givenCreateCategory_whenSaveFails_thenThrowException() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                null,
                CategoryStatus.ACTIVE
        );

        // When
        Mockito.when(categoryRepository.save(ArgumentMatchers.any(CategoryEntity.class)))
                .thenThrow(RuntimeException.class);

        // Then
        Assertions.assertThrows(RuntimeException.class,
                () -> categoryService.createCategory(createCommand));

        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenCreateCategory_whenCategoryAlreadyExists_thenThrowException() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "Category",
                CategoryStatus.ACTIVE
        );

        // When
        Mockito.when(categoryRepository.existsByName(createCommand.name())).thenReturn(true);

        // Then
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(createCommand));

        // Verify
        Mockito.verify(categoryRepository, Mockito.times(1))
                .existsByName(createCommand.name());

        Mockito.verify(categoryRepository, Mockito.never())
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenCreateCategory_thenSaveCategoryEntity() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "TestCategory",
                CategoryStatus.ACTIVE
        );
        CategoryEntity categoryEntity = categoryCreateCommandToEntityMapper.map(createCommand);

        // When
        Mockito.when(categoryRepository.save(ArgumentMatchers.any(CategoryEntity.class)))
                .thenReturn(categoryEntity);

        categoryService.createCategory(createCommand);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenUpdateCategory_whenCategoryExists_thenUpdateCategoryEntity() {
        // Given
        Long categoryId = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.INACTIVE
        );
        CategoryEntity categoryEntity = categoryUpdateCommandToEntityMapper.map(updateCommand);

        // When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.updateCategory(categoryId, updateCommand);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenUpdateCategory_whenCategoryIdDoesNotExists_thenThrowCategoryNotFoundException() {
        // Given
        Long categoryId = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.INACTIVE
        );

        // When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, updateCommand));
    }

    @Test
    void givenSoftDeleteCategory_whenCategoryExists_thenUpdatedCategoryEntity() {
        // Given
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();

        // When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.deleteCategory(categoryId);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenSoftDeleteCategory_whenCategoryIdDoesNotExists_thenThrowCategoryNotFoundException() {
        //Given
        Long categoryId = 1L;

        //When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));
    }
}