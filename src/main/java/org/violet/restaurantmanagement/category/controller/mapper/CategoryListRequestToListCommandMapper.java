package org.violet.restaurantmanagement.category.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.category.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;

@Mapper
public interface CategoryListRequestToListCommandMapper extends BaseMapper<CategoryListRequest, CategoryListCommand> {

    CategoryListRequestToListCommandMapper INSTANCE = Mappers.getMapper(CategoryListRequestToListCommandMapper.class);
}
