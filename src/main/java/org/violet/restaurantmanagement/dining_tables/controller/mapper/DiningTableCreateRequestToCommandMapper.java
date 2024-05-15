package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;

@Mapper
public interface DiningTableCreateRequestToCommandMapper extends BaseMapper<DiningTableCreateRequest, DiningTableCreateCommand> {

    DiningTableCreateRequestToCommandMapper INSTANCE = Mappers.getMapper(DiningTableCreateRequestToCommandMapper.class);
}
