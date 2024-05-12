package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.Sorting;
import org.violet.restaurantmanagement.product.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.mapper.CategoryCreateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.product.model.mapper.CategoryUpdateCommandToEntityMapper;
import org.violet.restaurantmanagement.product.repository.CategoryRepository;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;
import org.violet.restaurantmanagement.product.util.RmaServiceTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class CategoryServiceImplTest extends RmaServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;

    private static final CategoryUpdateCommandToEntityMapper categoryUpdateCommandToEntityMapper = CategoryUpdateCommandToEntityMapper.INSTANCE;

    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;

    @Test
    void givenGetAllCategories_whenCategoryListWithoutFilterAndSorting_thenReturnCategories() {
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(1L, "Category 1", CategoryStatus.ACTIVE));
        categoryEntities.add(new CategoryEntity(2L, "Category 2", CategoryStatus.DELETED));

        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        CategoryListCommand givenCategoryListCommand = CategoryListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .build();

        // When
        Mockito.when(categoryRepository.findAll(
                Mockito.<Specification<CategoryEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(categoryEntityPage);

        RmaPage<Category> result = categoryService.getAllCategories(givenCategoryListCommand);

        // Assertions
        Assertions.assertEquals(categoryEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllCategories_whenCategoryListWithoutSorting_thenReturnCategories() {
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(1L, "Category 1", CategoryStatus.ACTIVE));
        categoryEntities.add(new CategoryEntity(2L, "Category 2", CategoryStatus.DELETED));

        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        CategoryListCommand givenCategoryListCommand = CategoryListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .filter(CategoryListCommand.CategoryFilter.builder()
                        .name("Category")
                        .statuses(Collections.singleton(CategoryStatus.ACTIVE)).build())
                .build();

        // When
        Mockito.when(categoryRepository.findAll(
                Mockito.<Specification<CategoryEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(categoryEntityPage);

        RmaPage<Category> result = categoryService.getAllCategories(givenCategoryListCommand);

        // Assertions
        Mockito.verify(categoryRepository, Mockito.never()).findAll();

        Assertions.assertEquals(categoryEntities.size(), result.getContent().size());
        Assertions.assertEquals(result.getContent().get(0).getName(), categoryEntities.get(0).getName());
        Assertions.assertEquals(result.getContent().get(1).getName(), categoryEntities.get(1).getName());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), categoryEntities.size());
        Assertions.assertEquals(categoryEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllCategories_whenCategoryListExist_thenReturnCategories() {
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(1L, "Category 1", CategoryStatus.ACTIVE));
        categoryEntities.add(new CategoryEntity(2L, "Category 2", CategoryStatus.DELETED));
        categoryEntities.add(new CategoryEntity(2L, "Category 3", CategoryStatus.DELETED));

        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        CategoryListCommand givenCategoryListCommand = CategoryListCommand.builder()
                .pagination(Pagination.builder().pageNumber(1).pageSize(5).build())
                .sorting(Sorting.builder().direction(Sort.Direction.ASC).property("name").build())
                .filter(CategoryListCommand.CategoryFilter.builder()
                        .name("Category")
                        .statuses(Collections.singleton(CategoryStatus.ACTIVE)).build())
                .build();

        Mockito.when(categoryRepository.findAll(
                Mockito.<Specification<CategoryEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(categoryEntityPage);

        RmaPage<Category> result = categoryService.getAllCategories(givenCategoryListCommand);

        // Assertions
        Mockito.verify(categoryRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getName(), categoryEntities.get(0).getName());
        Assertions.assertEquals(result.getContent().get(1).getName(), categoryEntities.get(1).getName());
        Assertions.assertEquals(result.getContent().get(2).getName(), categoryEntities.get(2).getName());

        Assertions.assertEquals(3, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(), categoryEntities.size());
        Assertions.assertEquals(categoryEntities.size(), result.getContent().size());
    }

    @Test
    void givenGetAllCategories_whenResultIsNull_thenThrowNullPointerException() {
        // When

        CategoryListCommand categoryListCommand = CategoryListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> categoryService.getAllCategories(categoryListCommand)
        );
    }

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
        Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
                .thenThrow(CategoryNotFoundException.class);

        // Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.createCategory(createCommand));

        Mockito.verify(categoryRepository, times(1))
                .save(any(CategoryEntity.class));
    }

    @Test
    void givenCreateCategory_whenCategoryAlreadyExists_thenThrowException() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "Category",
                CategoryStatus.ACTIVE
        );

        // When
        Mockito.when(categoryRepository.findByName(createCommand.name())).thenReturn(true);

        // Then
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(createCommand));

        // Verify
        Mockito.verify(categoryRepository, times(1))
                .findByName(createCommand.name());

        Mockito.verify(categoryRepository, Mockito.never())
                .save(any(CategoryEntity.class));
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
        Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
                .thenReturn(categoryEntity);

        categoryService.createCategory(createCommand);

        // Then
        Mockito.verify(categoryRepository, times(1))
                .save(any(CategoryEntity.class));
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
        Mockito.verify(categoryRepository, times(1))
                .save(any(CategoryEntity.class));
    }

    @Test
    void givenUpdateCategory_whenWithExistingName_thenThrowCategoryAlreadyExistsException() {
        // Given
        Long categoryId = 1L;
        String existingName = "Category";
        CategoryUpdateCommand command = new CategoryUpdateCommand(existingName, CategoryStatus.ACTIVE);

        // When
        Mockito.when(categoryRepository.findByName(existingName)).thenReturn(true);

        CategoryEntity categoryEntity = new CategoryEntity();
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryEntity));

        // Then
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(categoryId, command));

        // Verify
        Mockito.verify(categoryRepository).findByName(existingName);
        Mockito.verify(categoryRepository, Mockito.never()).save(any());
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
        Mockito.verify(categoryRepository, times(1))
                .save(any(CategoryEntity.class));
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