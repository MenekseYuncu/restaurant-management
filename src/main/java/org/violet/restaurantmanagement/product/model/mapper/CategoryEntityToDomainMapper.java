package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Mapper
public interface CategoryEntityToDomainMapper extends BaseMapper<CategoryEntity, Category> {

    CategoryEntityToDomainMapper INSTANCE = Mappers.getMapper(CategoryEntityToDomainMapper.class);

    @Override
    @Mapping(target = "createdAt", source = "createdAt")
    Category map(CategoryEntity entity);
}
