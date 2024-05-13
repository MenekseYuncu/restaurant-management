package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductUpdateRequest;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;

@Mapper
public interface ProductUpdateRequestToProductUpdateCommandMapper extends BaseMapper<ProductUpdateRequest, ProductUpdateCommand> {

    ProductUpdateRequestToProductUpdateCommandMapper INSTANCE = Mappers.getMapper(ProductUpdateRequestToProductUpdateCommandMapper.class);

}
