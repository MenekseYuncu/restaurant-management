package org.violet.restaurantmanagement.menu.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.menu.controller.response.MenuResponse;
import org.violet.restaurantmanagement.menu.service.domain.Menu;

@Mapper
public interface MenuToMenuResponseMapper extends BaseMapper<Menu, MenuResponse> {

    MenuToMenuResponseMapper INSTANCE = Mappers.getMapper(MenuToMenuResponseMapper.class);

    @Mapping(source = "product.id", target = "product.id")
    @Mapping(source = "product.name", target = "product.name")
    @Mapping(source = "product.ingredient", target = "product.ingredient")
    @Mapping(source = "product.price", target = "product.price")
    @Mapping(source = "product.currency", target = "product.currency")
    @Mapping(source = "product.status", target = "product.status")
    @Mapping(source = "product.extent", target = "product.extent")
    @Mapping(source = "product.extentType", target = "product.extentType")
    @Mapping(source = "category", target = "category")
    MenuResponse map(Menu source);
}
