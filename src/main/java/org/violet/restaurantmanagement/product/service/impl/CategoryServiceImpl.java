package org.violet.restaurantmanagement.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.common.pegable.Filtering;
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
import org.violet.restaurantmanagement.product.service.CategoryService;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.product.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final CategoryEntityToDomainMapper categoryEntityToDomainMapper = CategoryEntityToDomainMapper.INSTANCE;
    private static final CategoryCreateCommandToEntityMapper categoryCreateCommandToEntityMapper = CategoryCreateCommandToEntityMapper.INSTANCE;
    private static final CategoryUpdateCommandToEntityMapper categoryUpdateCommandToEntityMapper = CategoryUpdateCommandToEntityMapper.INSTANCE;


    @Override
    public PageContent<Category> getAllCategories(CategoryListCommand categoryListCommand) {
        PageRequest pageable = createPageRequest(categoryListCommand);
        Specification<CategoryEntity> specification = createSpecification(categoryListCommand.getFilter());

        Page<CategoryEntity> categoryPage = categoryRepository.findAll(specification, pageable);
        List<Category> content = categoryPage.getContent().stream()
                .map(categoryEntityToDomainMapper::map)
                .toList();

        return buildPageContent(categoryPage, content, categoryListCommand.getFilter(), categoryListCommand.getSorting());
    }

    private PageRequest createPageRequest(CategoryListCommand categoryListCommand) {
        Pagination pagination = categoryListCommand.getPagination();
        if (categoryListCommand.getSorting() != null && categoryListCommand.getSorting().getOrderBy() != null) {
            return PageRequest.of(
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize(),
                    Sort.by(categoryListCommand.getSorting().getOrder(),
                            categoryListCommand.getSorting().getOrderBy())
            );
        } else {
            return PageRequest.of(pagination.getPageNumber() - 1, pagination.getPageSize());
        }
    }

    private Specification<CategoryEntity> createSpecification(CategoryFilter filter) {
        return filter != null ? filter.toSpecification() : null;
    }

    private PageContent<Category> buildPageContent(Page<CategoryEntity> categoryPage,
                                                   List<Category> content,
                                                   Filtering filter,
                                                   Sorting sorting) {
        return PageContent.<Category>builder()
                .content(content)
                .pageNumber(categoryPage.getNumber() + 1)
                .pageSize(categoryPage.getSize())
                .totalPageCount(categoryPage.getTotalPages())
                .totalElementCount(categoryPage.getTotalElements())
                .sortedBy(sorting != null ? List.of(sorting) : null)
                .filteredBy(filter)
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

        if (categoryRepository.existsByName(categoryEntity.getName())) {
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

        if (categoryRepository.existsByName(updatedEntity.getName())){
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
