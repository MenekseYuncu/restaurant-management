package org.violet.restaurantmanagement.dining_tables.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

@Mapper
public interface DiningTableUpdateCommandToDomainMapper extends BaseMapper<DiningTableUpdateCommand, DiningTable> {

    DiningTableUpdateCommandToDomainMapper INSTANCE = Mappers.getMapper(DiningTableUpdateCommandToDomainMapper.class);
}
