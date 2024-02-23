package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.violet.restaurantmanagement.product.model.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Mapper(componentModel = "spring")
public interface CategoryEntityToCategoryMapper {

    @Mapping(target = "updatedAt", ignore = true)
    Category mapToDto(CategoryEntity categoryEntity);
}
