package org.violet.restaurantmanagement.menu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.menu.service.domain.Menu;
import org.violet.restaurantmanagement.product.service.domain.Product;

@Mapper
public interface ProductToMenuMapper extends BaseMapper<Product, Menu> {

    ProductToMenuMapper INSTANCE = Mappers.getMapper(ProductToMenuMapper.class);

    @Mapping(source = "id", target = "product.id")
    @Mapping(source = "name", target = "product.name")
    @Mapping(source = "ingredient", target = "product.ingredient")
    @Mapping(source = "price", target = "product.price")
    @Mapping(source = "currency", target = "product.currency")
    @Mapping(source = "status", target = "product.status")
    @Mapping(source = "extent", target = "product.extent")
    @Mapping(source = "extentType", target = "product.extentType")
    @Mapping(source = "category", target = "category")
    Menu map(Product source);
}
