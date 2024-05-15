package org.violet.restaurantmanagement.dining_tables.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class DiningTableServiceImpl implements DiningTableService {

    private final DiningTableRepository diningTableRepository;

    private static final DiningTableCreateCommandToDomainMapper diningTableCreateCommandToDomainMapper = DiningTableCreateCommandToDomainMapper.INSTANCE;
    private static final DomainToDiningTableEntityMapper domainToDiningTableEntityMapper = DomainToDiningTableEntityMapper.INSTANCE;
    private static final DiningTableUpdateCommandToDomainMapper diningTableUpdateCommandToDomainMapper = DiningTableUpdateCommandToDomainMapper.INSTANCE;


    @Override
    public void createDiningTables(DiningTableCreateCommand createCommand) {
        List<DiningTable> diningTables = new ArrayList<>();

        for (int i = 0; i < createCommand.numberOfTables(); i++) {
            DiningTable diningTable = diningTableCreateCommandToDomainMapper.map(createCommand);
            diningTable.merge();
            diningTable.tableStatus();
            diningTables.add(diningTable);
        }

        List<DiningTableEntity> diningTableEntities = domainToDiningTableEntityMapper.map(diningTables);
        diningTableRepository.saveAll(diningTableEntities);
    }


    @Override
    public void updateDiningTable(Long id, DiningTableUpdateCommand updateCommand) {

        DiningTableEntity diningTableEntity = diningTableRepository.findById(id)
                .orElseThrow(DiningTableNotFoundException::new);

        DiningTable updateTable = diningTableUpdateCommandToDomainMapper.map(updateCommand);

        diningTableEntity.setStatus(updateTable.getStatus());
        diningTableEntity.setSize(updateTable.getSize());

        diningTableRepository.save(diningTableEntity);
    }
}
