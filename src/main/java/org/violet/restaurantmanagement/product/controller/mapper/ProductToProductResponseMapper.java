package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.response.ProductResponse;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductToProductResponseMapper extends BaseMapper<Product, ProductResponse> {

    ProductToProductResponseMapper INSTANCE = Mappers.getMapper(ProductToProductResponseMapper.class);
}
