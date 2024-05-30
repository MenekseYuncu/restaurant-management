package org.violet.restaurantmanagement.category.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.category.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.exceptions.CategoryStatusAlreadyChangedException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.model.mapper.CategoryCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.category.model.mapper.CategoryEntityToDomainMapper;
import org.violet.restaurantmanagement.category.model.mapper.CategoryToCategoryEntityMapper;
import org.violet.restaurantmanagement.category.repository.CategoryRepository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


class CategoryServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final CategoryCreateCommandToDomainMapper categoryCreateCommandToDomainMapper = CategoryCreateCommandToDomainMapper.INSTANCE;
    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;
    private static final CategoryToCategoryEntityMapper categoryToCategoryEntityMapper = CategoryToCategoryEntityMapper.INSTANCE;


    @Test
    void givenCategoryListWithoutFilterAndSorting_whenGetAllCategories_thenReturnCategories() {
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(
                1L,
                "Category 1",
                CategoryStatus.ACTIVE)
        );
        categoryEntities.add(new CategoryEntity(
                2L,
                "Category 2",
                CategoryStatus.DELETED)
        );

        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        CategoryListCommand givenCategoryListCommand = CategoryListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(2)
                        .build()
                )
                .build();

        // When
        Mockito.when(categoryRepository.findAll(
                Mockito.<Specification<CategoryEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(categoryEntityPage);

        RmaPage<Category> result = categoryService.getAllCategories(givenCategoryListCommand);

        // Assertions
        Assertions.assertEquals(result.getTotalElementCount(), categoryEntities.size());
        Assertions.assertEquals(categoryEntities.size(), result.getContent().size());
    }

    @Test
    void givenCategoryListWithoutSorting_whenGetAllCategories_thenReturnCategories() {
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(
                1L,
                "Category 1",
                CategoryStatus.ACTIVE)
        );
        categoryEntities.add(new CategoryEntity(
                2L,
                "Category 2",
                CategoryStatus.DELETED)
        );

        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        CategoryListCommand givenCategoryListCommand = CategoryListCommand.builder()
                .pagination(PaginationBuilder.builder()
                        .pageNumber(1)
                        .pageSize(2)
                        .build()
                )
                .filter(CategoryListCommand.CategoryFilter.builder()
                        .name("Category")
                        .statuses(Collections.singleton(CategoryStatus.ACTIVE))
                        .build()
                )
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
    void givenCategoryListExist_whenGetAllCategories_thenReturnCategories() {
        // Given
        CategoryListCommand.CategoryFilter mockCategoryFilter = CategoryListCommand.CategoryFilter.builder()
                .name("Category")
                .build();

        CategoryListCommand mockGivenCategoryListCommand = CategoryListCommand.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .asc()
                                .property("name")
                                .build()
                )
                .filter(mockCategoryFilter)
                .build();

        // When
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(
                1L,
                "Category 1",
                CategoryStatus.ACTIVE)
        );
        categoryEntities.add(new CategoryEntity(
                2L,
                "Category 2",
                CategoryStatus.DELETED)
        );
        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(categoryEntities);

        Mockito.when(categoryRepository.findAll(
                Mockito.<Specification<CategoryEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(categoryEntityPage);

        // Then
        RmaPage<Category> result = categoryService.getAllCategories(mockGivenCategoryListCommand);

        // Assertions
        Mockito.verify(categoryRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getName(), categoryEntities.get(0).getName());
        Assertions.assertEquals(result.getContent().get(1).getName(), categoryEntities.get(1).getName());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(),
                mockGivenCategoryListCommand.getPagination().getPageSize()
        );
        Assertions.assertEquals(result.getFilteredBy(), mockGivenCategoryListCommand.getFilter());
    }

    @Test
    void givenResultIsNull_whenGetAllCategories_thenThrowNullPointerException() {
        // When
        CategoryListCommand categoryListCommand = CategoryListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> categoryService.getAllCategories(categoryListCommand)
        );
    }

    @Test
    void givenCategoryExists_whenGetCategoryById_thenReturnCategory() {
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
    void givenCategoryDoesNotExist_whenGetCategoryById_thenThrowCategoryNotFoundException() {
        //Given
        Long categoryId = 1L;

        //When
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void givenValidCreateCategoryCommand_whenCreateCategory_thenReturnSuccess() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "Category",
                CategoryStatus.ACTIVE
        );

        // When
        Mockito.when(categoryRepository.findByName(createCommand.name()))
                .thenReturn(Optional.empty());

        categoryService.createCategory(createCommand);

        // Verify
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(Mockito.any(CategoryEntity.class));
    }

    @Test
    void givenInvalidCreateCategoryCommand_whenCreateCategory_thenThrowException() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                null,
                CategoryStatus.ACTIVE
        );

        // When
        Mockito.when(categoryRepository.save(ArgumentMatchers.any(CategoryEntity.class)))
                .thenThrow(CategoryNotFoundException.class);

        // Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.createCategory(createCommand));

        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenCategoryAlreadyExists_whenCreateCategory_ThrowsException() {
        // Given
        CategoryCreateCommand createCommand = new CategoryCreateCommand(
                "Category",
                CategoryStatus.ACTIVE
        );

        // When
        Category category = categoryCreateCommandToDomainMapper.map(createCommand);
        CategoryEntity categoryEntity = categoryToCategoryEntityMapper.map(category);

        Mockito.when(categoryRepository.findByName(createCommand.name()))
                .thenReturn(Optional.of(categoryEntity));

        // Assert
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(createCommand));

        // Verify
        Mockito.verify(categoryRepository, Mockito.never())
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenValidUpdateCategoryCommand_whenUpdateCategory_thenReturnSuccess() {
        // Given
        Long id = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "Updated Category",
                CategoryStatus.ACTIVE
        );

        CategoryEntity existingCategoryEntity = new CategoryEntity();
        existingCategoryEntity.setId(id);
        existingCategoryEntity.setName("Old Category");
        existingCategoryEntity.setStatus(CategoryStatus.INACTIVE);

        // When
        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existingCategoryEntity));

        // Act
        categoryService.updateCategory(id, updateCommand);

        // Verify
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(existingCategoryEntity);

        Assertions.assertEquals("Updated Category", existingCategoryEntity.getName());
        Assertions.assertEquals(CategoryStatus.ACTIVE, existingCategoryEntity.getStatus());
    }

    @Test
    void givenSameStatusAndName_whenUpdateCategory_thenThrowException() {
        // Given
        Long id = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "Same Category",
                CategoryStatus.ACTIVE
        );

        CategoryEntity existingCategoryEntity = new CategoryEntity();
        existingCategoryEntity.setId(id);
        existingCategoryEntity.setName("Same Category");
        existingCategoryEntity.setStatus(CategoryStatus.ACTIVE);

        // When
        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existingCategoryEntity));

        // Then
        Assertions.assertThrows(CategoryStatusAlreadyChangedException.class,
                () -> categoryService.updateCategory(id, updateCommand));

        Mockito.verify(categoryRepository, Mockito.times(0))
                .save(existingCategoryEntity);
    }

    @Test
    void givenInValidCategoryId_whenUpdateCategory_thenReturnCategoryNotFound() {
        // Given
        Long id = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "Updated Category",
                CategoryStatus.ACTIVE
        );

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(id, updateCommand));

        // Verify
        Mockito.verify(categoryRepository, Mockito.never())
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenInValidCategoryName_whenUpdateCategory_thenReturnCategoryNameAlreadyExists() {
        // Given
        Long categoryId = 1L;
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(
                "Category test",
                CategoryStatus.ACTIVE
        );
        CategoryEntity existingCategoryEntity = new CategoryEntity();
        existingCategoryEntity.setId(categoryId);
        existingCategoryEntity.setName("Category");

        CategoryEntity anotherCategoryEntity = new CategoryEntity();
        anotherCategoryEntity.setId(2L);
        anotherCategoryEntity.setName("Category test");

        // When
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(existingCategoryEntity));

        Mockito.when(categoryRepository.findByName("Category test"))
                .thenReturn(Optional.of(existingCategoryEntity));

        // Assert
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(categoryId, updateCommand));

        // Verify
        Mockito.verify(categoryRepository, Mockito.never())
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenCategoryExistsAndNotDeleted_whenSoftDeleteCategory_thenReturnSuccess() {
        // Given
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setStatus(CategoryStatus.ACTIVE);

        // When
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryEntity));

        categoryService.deleteCategory(categoryId);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }

    @Test
    void givenCategoryExistsAndAlreadyDeleted_whenSoftDeleteCategory_thenThrowException() {
        // Given
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setStatus(CategoryStatus.DELETED);

        // When
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryEntity));

        // Then
        Assertions.assertThrows(CategoryStatusAlreadyChangedException.class,
                () -> categoryService.deleteCategory(categoryId));

        Mockito.verify(categoryRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(CategoryEntity.class));
    }


    @Test
    void givenCategoryIdDoesNotExists_whenSoftDeleteCategory_thenThrowCategoryNotFoundException() {
        //Given
        Long categoryId = 1L;

        //When
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));
    }
}