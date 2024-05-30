package org.violet.restaurantmanagement.product.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.controller.response.ProductListResponse;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductToProductListResponseMapper extends BaseMapper<Product, ProductListResponse> {

    ProductToProductListResponseMapper INSTANCE = Mappers.getMapper(ProductToProductListResponseMapper.class);
}
