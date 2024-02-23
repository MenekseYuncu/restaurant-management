package org.violet.restaurantmanagement.product.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.product.category.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

@Mapper(componentModel = "spring")
public interface CategoryToCategoryResponseMapper {
    CategoryToCategoryResponseMapper INSTANCE = Mappers.getMapper(CategoryToCategoryResponseMapper.class);

    CategoryResponse map(Category category);
}
