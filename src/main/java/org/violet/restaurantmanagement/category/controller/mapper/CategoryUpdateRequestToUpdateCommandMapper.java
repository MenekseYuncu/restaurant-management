package org.violet.restaurantmanagement.category.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.category.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;

@Mapper
public interface CategoryUpdateRequestToUpdateCommandMapper extends BaseMapper<CategoryUpdateRequest, CategoryUpdateCommand> {

    CategoryUpdateRequestToUpdateCommandMapper INSTANCE = Mappers.getMapper(CategoryUpdateRequestToUpdateCommandMapper.class);

}
