package org.violet.restaurantmanagement.product.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductDomainToEntityMapper extends BaseMapper<Product, ProductEntity> {

    ProductDomainToEntityMapper INSTANCE = Mappers.getMapper(ProductDomainToEntityMapper.class);
}
