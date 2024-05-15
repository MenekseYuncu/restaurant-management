package org.violet.restaurantmanagement.dining_tables.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

@Mapper
public interface DomainToDiningTableEntityMapper extends BaseMapper<DiningTable, DiningTableEntity> {

    DomainToDiningTableEntityMapper INSTANCE = Mappers.getMapper(DomainToDiningTableEntityMapper.class);
}
