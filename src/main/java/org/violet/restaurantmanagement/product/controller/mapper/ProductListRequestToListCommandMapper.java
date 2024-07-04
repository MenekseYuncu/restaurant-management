package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductListRequest;
import org.violet.restaurantmanagement.product.service.command.ProductListCommand;

@Mapper
public interface ProductListRequestToListCommandMapper extends BaseMapper<ProductListRequest, ProductListCommand> {

    ProductListRequestToListCommandMapper INSTANCE = Mappers.getMapper(ProductListRequestToListCommandMapper.class);
}
