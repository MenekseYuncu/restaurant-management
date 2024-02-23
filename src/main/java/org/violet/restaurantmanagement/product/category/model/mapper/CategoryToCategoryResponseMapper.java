package org.violet.restaurantmanagement.product.category.model.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.product.category.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

@Mapper(componentModel = "spring")
public interface CategoryToCategoryResponseMapper {

    CategoryResponse map(Category category);
}
