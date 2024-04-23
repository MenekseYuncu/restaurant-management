package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.product.service.command.CategoryCreateCommand;

@Mapper
public interface CategoryCreateRequestToCreateCommandMapper extends BaseMapper<CategoryCreateRequest, CategoryCreateCommand> {

    CategoryCreateRequestToCreateCommandMapper INSTANCE = Mappers.getMapper(CategoryCreateRequestToCreateCommandMapper.class);

}
