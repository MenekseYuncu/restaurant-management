package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableSplitRequest;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableSplitCommand;

@Mapper
public interface DiningTableSplitRequestToCommandMapper extends BaseMapper<DiningTableSplitRequest, DiningTableSplitCommand> {

    DiningTableSplitRequestToCommandMapper INSTANCE = Mappers.getMapper(DiningTableSplitRequestToCommandMapper.class);
}
