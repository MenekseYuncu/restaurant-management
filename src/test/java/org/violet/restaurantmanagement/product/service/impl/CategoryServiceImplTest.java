package org.violet.restaurantmanagement.product.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.common.pegable.PageContent;
import org.violet.restaurantmanagement.common.pegable.Pagination;
import org.violet.restaurantmanagement.common.pegable.Sorting;
import org.violet.restaurantmanagement.product.controller.util.CategoryFilter;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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
    void givenGetAllCategories_whenSingleCategorySuccessfully_thenReturnCategory() {
        // Given
        Pagination pagination = Pagination.builder()
                .pageNumber(1).pageSize(10).build();

        CategoryFilter filter = CategoryFilter.builder()
                .name("Test").statuses(Collections.singleton(CategoryStatus.ACTIVE)).build();

        Sorting sorting = Sorting.builder()
                .orderBy("name").order(Sort.Direction.ASC).build();

        CategoryListCommand command = CategoryListCommand.builder()
                .pagination(pagination).filter(filter).sorting(sorting).build();

        // When
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(1L).name("Food").status(CategoryStatus.ACTIVE).build();

        List<CategoryEntity> categoryEntities = List.of(categoryEntity);

        Page<CategoryEntity> page = new PageImpl<>(categoryEntities,
                PageRequest.of(0, 5), 1);

        Mockito.when(categoryRepository.findAll(ArgumentMatchers.<Specification<CategoryEntity>>any(),
                any(PageRequest.class))).thenReturn(page);

        // Then
        PageContent<Category> result = categoryService.getAllCategories(command);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Food", result.getContent().getFirst().getName());
        Assertions.assertEquals(1, result.getTotalPageCount());

    }

    @Test
    void givenGetAllCategories_whenMultipleCategoriesSuccessfully_thenReturnCategories() {
        // Given
        Pagination pagination = Pagination.builder()
                .pageNumber(1).pageSize(2).build();

        CategoryFilter filter = CategoryFilter.builder()
                .name("Food").statuses(Collections.singleton(CategoryStatus.ACTIVE)).build();

        Sorting sorting = Sorting.builder()
                .orderBy("name").order(Sort.Direction.ASC).build();

        CategoryListCommand command = CategoryListCommand.builder()
                .pagination(pagination).filter(filter).sorting(sorting).build();

        // When
        List<CategoryEntity> categoryEntities = Arrays.asList(
                new CategoryEntity(1L, "Test Category 1", CategoryStatus.ACTIVE),
                new CategoryEntity(2L, "Test Category 2", CategoryStatus.ACTIVE)
        );
        Page<CategoryEntity> page = new PageImpl<>(categoryEntities,
                PageRequest.of(1, 5), 3);

        Mockito.when(categoryRepository.findAll(ArgumentMatchers.<Specification<CategoryEntity>>any(),
                any(PageRequest.class))).thenReturn(page);

        // Then
        PageContent<Category> result = categoryService.getAllCategories(command);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Test Category 1", result.getContent().get(0).getName());
        Assertions.assertEquals("Test Category 2", result.getContent().get(1).getName());
        Assertions.assertEquals(2, result.getTotalPageCount());
    }

    @Test
    void givenGetAllCategories_whenPaginationInfoNotProvided_thenReturnsException() {
        // Given
        CategoryListCommand command = CategoryListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> categoryService.getAllCategories(command));
    }

    @Test
    void givenGetAllCategories_whenSortingInfoNotProvided_thenReturnException() {
        // Given
        Pagination pagination = Pagination.builder()
                .pageNumber(0).pageSize(10).build();

        CategoryFilter filter = CategoryFilter.builder()
                .name("Food").statuses(Collections.singleton(CategoryStatus.ACTIVE)).build();

        CategoryListCommand command = CategoryListCommand.builder()
                .pagination(pagination).filter(filter).build();

        // Then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> categoryService.getAllCategories(command));
    }

    @Test
    void testGetAllCategoriesThrowsExceptionWhenCategoryListCommandIsNull() {
        // Given
        CategoryListCommand categoryListCommand = null;

        // When/Then
        Assertions.assertThrows(NullPointerException.class,
                () -> categoryService.getAllCategories(categoryListCommand));
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
        when(categoryRepository.findById(categoryId))
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
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

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
        when(categoryRepository.save(any(CategoryEntity.class)))
                .thenThrow(CategoryNotFoundException.class);

        // Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.createCategory(createCommand));

        Mockito.verify(categoryRepository, Mockito.times(1))
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
        when(categoryRepository.findByName(createCommand.name())).thenReturn(true);

        // Then
        Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(createCommand));

        // Verify
        Mockito.verify(categoryRepository, Mockito.times(1))
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
        when(categoryRepository.save(any(CategoryEntity.class)))
                .thenReturn(categoryEntity);

        categoryService.createCategory(createCommand);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
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
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.updateCategory(categoryId, updateCommand);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(any(CategoryEntity.class));
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
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

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
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        categoryService.deleteCategory(categoryId);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(any(CategoryEntity.class));
    }

    @Test
    void givenSoftDeleteCategory_whenCategoryIdDoesNotExists_thenThrowCategoryNotFoundException() {
        //Given
        Long categoryId = 1L;

        //When
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));
    }
}