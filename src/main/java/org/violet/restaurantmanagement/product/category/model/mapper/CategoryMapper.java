package org.violet.restaurantmanagement.product.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.product.category.model.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.category.service.domain.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "updatedAt", ignore = true)
    Category mapToDto(CategoryEntity categoryEntity);
}
