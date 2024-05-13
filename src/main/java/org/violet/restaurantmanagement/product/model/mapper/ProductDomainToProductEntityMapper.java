package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductDomainToProductEntityMapper extends BaseMapper<Product, ProductEntity> {

    ProductDomainToProductEntityMapper INSTANCE = Mappers.getMapper(ProductDomainToProductEntityMapper.class);
}
