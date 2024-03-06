package org.violet.restaurantmanagement.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;
import org.violet.restaurantmanagement.product.service.impl.CategoryServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;

    private static final CategoryUpdateCommandToEntityMapper categoryUpdateCommandToEntityMapper = CategoryUpdateCommandToEntityMapper.INSTANCE;


    @Test
    void testGetCategoryById_WhenCategoryExists_ShouldReturnCategory() {

        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setName("TestCategory");
        categoryEntity.setStatus(CategoryStatus.ACTIVE);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        Category resultCategory = categoryService.getCategoryById(categoryId);

        assertNotNull(resultCategory);
        assertEquals(categoryId, resultCategory.getId());
        assertEquals("TestCategory", resultCategory.getName());
        assertEquals(CategoryStatus.ACTIVE, resultCategory.getStatus());
    }

    @Test
    void testGetCategoryById_WhenCategoryDoesNotExist_ShouldThrowCategoryNotFoundException() {

        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void testCreateCategory_whenSaveFails_shouldThrowException() {

        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                null,
                CategoryStatus.ACTIVE
        );

        when(categoryRepository.save(any(CategoryEntity.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> categoryService.createCategory(createCommand));

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void testCreateCategory_shouldSaveCategoryEntity() {

        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "TestCategory",
                CategoryStatus.ACTIVE
        );
        CategoryEntity categoryEntity = categoryCreateCommandToEntityMapper.map(createCommand);

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        categoryService.createCategory(createCommand);

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void testUpdateCategory_WhenCategoryExists_shouldUpdateCategoryEntity() {

        Long categoryId = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.INACTIVE
        );
        CategoryEntity categoryEntity = categoryUpdateCommandToEntityMapper.map(updateCommand);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.updateCategory(categoryId, updateCommand);

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void testUpdateCategory_WhenCategoryIdDoesNotExists_ShouldThrowCategoryNotFoundException() {

        Long categoryId = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "UpdateTest",
                CategoryStatus.INACTIVE
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, updateCommand));
    }

    @Test
    void testSoftDeleteCategory_WhenCategoryExists_ShouldUpdatedCategoryEntity(){

        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void testSoftDeleteCategory_WhenCategoryIdDoesNotExists_ShouldThrowCategoryNotFoundException(){

        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));
    }
}