package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;

@Mapper
public interface CategoryListRequestToListCommandMapper extends BaseMapper<CategoryListRequest, CategoryListCommand> {

    CategoryListRequestToListCommandMapper INSTANCE = Mappers.getMapper(CategoryListRequestToListCommandMapper.class);
}
