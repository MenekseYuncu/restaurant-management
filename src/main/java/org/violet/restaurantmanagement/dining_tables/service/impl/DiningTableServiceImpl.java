package org.violet.restaurantmanagement.dining_tables.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class DiningTableServiceImpl implements DiningTableService {

    private final DiningTableRepository diningTableRepository;

    private static final DiningTableCreateCommandToDomainMapper diningTableCreateCommandToDomainMapper = DiningTableCreateCommandToDomainMapper.INSTANCE;
    private static final DomainToDiningTableEntityMapper domainToDiningTableEntityMapper = DomainToDiningTableEntityMapper.INSTANCE;


    @Override
    public void createDiningTables(List<DiningTableCreateCommand> createCommand) {

        List<DiningTable> diningTables = diningTableCreateCommandToDomainMapper.map(createCommand);

        diningTables.forEach(
                diningTable -> diningTable.setMergeId(UUID.randomUUID().toString())
        );

        List<DiningTableEntity> diningTableEntity = domainToDiningTableEntityMapper.map(diningTables);

        diningTableRepository.saveAll(diningTableEntity);
    }
}
