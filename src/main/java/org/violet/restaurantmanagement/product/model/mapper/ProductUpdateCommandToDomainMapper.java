package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductUpdateCommandToDomainMapper extends BaseMapper<ProductUpdateCommand, Product> {

    ProductUpdateCommandToDomainMapper INSTANCE = Mappers.getMapper(ProductUpdateCommandToDomainMapper.class);
}
