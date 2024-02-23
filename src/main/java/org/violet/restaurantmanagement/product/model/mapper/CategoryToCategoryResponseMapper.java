package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.product.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Mapper(componentModel = "spring")
public interface CategoryToCategoryResponseMapper {

    CategoryResponse map(Category category);
}
