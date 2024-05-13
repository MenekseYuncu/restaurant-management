package org.violet.restaurantmanagement.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;

@Mapper
public interface CategoryUpdateCommandToEntityMapper extends BaseMapper<CategoryUpdateCommand, CategoryEntity> {

    CategoryUpdateCommandToEntityMapper INSTANCE = Mappers.getMapper(CategoryUpdateCommandToEntityMapper.class);

}
