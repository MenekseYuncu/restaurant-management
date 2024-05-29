package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableListRequest;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableListCommand;

@Mapper
public interface DiningTableListRequestToListCommandMapper extends BaseMapper<DiningTableListRequest, DiningTableListCommand> {

    DiningTableListRequestToListCommandMapper INSTANCE = Mappers.getMapper(DiningTableListRequestToListCommandMapper.class);
}
