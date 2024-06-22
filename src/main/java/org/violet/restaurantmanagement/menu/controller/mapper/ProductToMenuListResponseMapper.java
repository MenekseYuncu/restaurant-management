package org.violet.restaurantmanagement.menu.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.menu.controller.response.MenuListResponse;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductToMenuListResponseMapper extends BaseMapper<Product, MenuListResponse> {

    ProductToMenuListResponseMapper INSTANCE = Mappers.getMapper(ProductToMenuListResponseMapper.class);
}
