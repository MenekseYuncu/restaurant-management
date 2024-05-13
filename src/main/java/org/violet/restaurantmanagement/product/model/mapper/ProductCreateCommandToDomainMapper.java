package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductCreateCommandToDomainMapper extends BaseMapper<ProductCreateCommand, Product> {

    ProductCreateCommandToDomainMapper INSTANCE = Mappers.getMapper(ProductCreateCommandToDomainMapper.class);
}
