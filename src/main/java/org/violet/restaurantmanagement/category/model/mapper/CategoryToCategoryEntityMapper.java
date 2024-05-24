package org.violet.restaurantmanagement.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;

@Mapper
public interface CategoryToCategoryEntityMapper extends BaseMapper<Category, CategoryEntity> {

    CategoryToCategoryEntityMapper INSTANCE = Mappers.getMapper(CategoryToCategoryEntityMapper.class);
}
