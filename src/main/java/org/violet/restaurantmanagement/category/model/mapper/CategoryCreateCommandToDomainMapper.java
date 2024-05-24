package org.violet.restaurantmanagement.category.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;

@Mapper
public interface CategoryCreateCommandToDomainMapper extends BaseMapper<CategoryCreateCommand, Category> {

    CategoryCreateCommandToDomainMapper INSTANCE = Mappers.getMapper(CategoryCreateCommandToDomainMapper.class);

}
