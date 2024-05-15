package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;

@Mapper
public interface DiningTableUpdateRequestToCommandMapper extends BaseMapper<DiningTableUpdateRequest, DiningTableUpdateCommand> {

    DiningTableUpdateRequestToCommandMapper INSTANCE = Mappers.getMapper(DiningTableUpdateRequestToCommandMapper.class);
}
