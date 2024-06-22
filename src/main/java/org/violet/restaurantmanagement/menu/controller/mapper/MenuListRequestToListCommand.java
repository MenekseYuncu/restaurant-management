package org.violet.restaurantmanagement.menu.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.menu.controller.request.MenuListRequest;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;

@Mapper
public interface MenuListRequestToListCommand extends BaseMapper<MenuListRequest, MenuListCommand> {

    MenuListRequestToListCommand INSTANCE = Mappers.getMapper(MenuListRequestToListCommand.class);
}
