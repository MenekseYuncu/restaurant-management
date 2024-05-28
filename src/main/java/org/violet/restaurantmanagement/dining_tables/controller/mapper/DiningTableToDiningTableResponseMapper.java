package org.violet.restaurantmanagement.dining_tables.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.response.DiningTableResponse;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

@Mapper
public interface DiningTableToDiningTableResponseMapper extends BaseMapper<DiningTable, DiningTableResponse> {

    DiningTableToDiningTableResponseMapper INSTANCE = Mappers.getMapper(DiningTableToDiningTableResponseMapper.class);
}
