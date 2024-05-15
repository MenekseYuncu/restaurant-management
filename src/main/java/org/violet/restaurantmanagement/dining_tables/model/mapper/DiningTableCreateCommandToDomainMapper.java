package org.violet.restaurantmanagement.dining_tables.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

@Mapper
public interface DiningTableCreateCommandToDomainMapper extends BaseMapper<DiningTableCreateCommand, DiningTable> {

    DiningTableCreateCommandToDomainMapper INSTANCE = Mappers.getMapper(DiningTableCreateCommandToDomainMapper.class);
}
