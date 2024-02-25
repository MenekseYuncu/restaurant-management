package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;

@Mapper
public interface CategoryCreateCommandToEntityMapper extends BaseMapper<CategoryCreateCommand, CategoryEntity> {

    CategoryCreateCommandToEntityMapper INSTANCE = Mappers.getMapper(CategoryCreateCommandToEntityMapper.class);

}
