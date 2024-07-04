package org.violet.restaurantmanagement.category.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;

@Mapper
public interface CategoryCreateRequestToCommandMapper extends BaseMapper<CategoryCreateRequest, CategoryCreateCommand> {

    CategoryCreateRequestToCommandMapper INSTANCE = Mappers.getMapper(CategoryCreateRequestToCommandMapper.class);

}
