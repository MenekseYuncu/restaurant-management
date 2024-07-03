package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableMergeRequest;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableMergeCommand;

@Mapper
public interface DiningTableToMergeRequestToCommandMapper extends BaseMapper<DiningTableMergeRequest, DiningTableMergeCommand> {

    DiningTableToMergeRequestToCommandMapper INSTANCE = Mappers.getMapper(DiningTableToMergeRequestToCommandMapper.class);
}
