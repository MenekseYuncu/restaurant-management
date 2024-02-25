package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.response.CategoryResponse;
import org.violet.restaurantmanagement.product.service.domain.Category;

@Mapper
public interface CategoryToCategoryResponseMapper extends BaseMapper<Category, CategoryResponse> {

    CategoryToCategoryResponseMapper INSTANCE = Mappers.getMapper(CategoryToCategoryResponseMapper.class);

}
