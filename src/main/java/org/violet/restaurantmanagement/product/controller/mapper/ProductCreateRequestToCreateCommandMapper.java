package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;

@Mapper
public interface ProductCreateRequestToCreateCommandMapper extends BaseMapper<ProductCreateRequest, ProductCreateCommand> {

    ProductCreateRequestToCreateCommandMapper INSTANCE = Mappers.getMapper(ProductCreateRequestToCreateCommandMapper.class);
}
