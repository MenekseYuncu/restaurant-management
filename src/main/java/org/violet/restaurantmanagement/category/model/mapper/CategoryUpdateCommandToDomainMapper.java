package org.violet.restaurantmanagement.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;

@Mapper
public interface CategoryUpdateCommandToDomainMapper extends BaseMapper<CategoryUpdateCommand, Category> {

    CategoryUpdateCommandToDomainMapper INSTANCE = Mappers.getMapper(CategoryUpdateCommandToDomainMapper.class);

}
