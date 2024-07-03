package org.violet.restaurantmanagement.dining_tables.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableStatusAlreadyChangedException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableCreateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableEntityToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableListCommand;
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
    private static final DiningTableEntityToDomainMapper diningTableEntityToDomainMapper = DiningTableEntityToDomainMapper.INSTANCE;


    @Override
    public RmaPage<DiningTable> getAllDiningTables(DiningTableListCommand diningTableListCommand) {
        Page<DiningTableEntity> diningTableEntityPage = diningTableRepository.findAll(
                diningTableListCommand.toSpecification(DiningTableEntity.class),
                diningTableListCommand.toPageable()
        );

        return RmaPage.<DiningTable>builder()
                .content(diningTableEntityToDomainMapper.map(diningTableEntityPage.getContent()))
                .page(diningTableEntityPage)
                .sortedBy(diningTableListCommand.getSorting())
                .filteredBy(diningTableListCommand.getFilter())
                .build();
    }

    @Override
    public DiningTable getDiningTableById(Long id) {
        DiningTableEntity diningTableEntity = diningTableRepository.findById(id)
                .orElseThrow(DiningTableNotFoundException::new);

        return diningTableEntityToDomainMapper.map(diningTableEntity);
    }

    @Override
    public void createDiningTables(DiningTableCreateCommand createCommand) {
        List<DiningTable> diningTables = new ArrayList<>();

        for (int i = 0; i < createCommand.count(); i++) {
            DiningTable diningTable = diningTableCreateCommandToDomainMapper.map(createCommand);
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

        this.checkExistingStatus(diningTableEntity.getStatus(), updateTable.getStatus());

        diningTableEntity.setStatus(updateTable.getStatus());
        diningTableEntity.setSize(updateTable.getSize());

        diningTableRepository.save(diningTableEntity);

    }

    @Override
    public void changeStatusToVacant(Long id) {
        DiningTableEntity diningTableEntity = diningTableRepository.findById(id)
                .orElseThrow(DiningTableNotFoundException::new);

        this.checkIfStatusChanged(diningTableEntity.getStatus(), DiningTableStatus.VACANT);
        diningTableEntity.setStatus(DiningTableStatus.VACANT);

        diningTableRepository.save(diningTableEntity);
    }

    @Override
    public void changeStatusToOccupied(Long id) {
        DiningTableEntity diningTableEntity = diningTableRepository.findById(id)
                .orElseThrow(DiningTableNotFoundException::new);

        this.checkIfStatusChanged(diningTableEntity.getStatus(), DiningTableStatus.OCCUPIED);
        diningTableEntity.setStatus(DiningTableStatus.OCCUPIED);

        diningTableRepository.save(diningTableEntity);
    }

    private void checkIfStatusChanged(DiningTableStatus entityStatus, DiningTableStatus status) {
        if (entityStatus == status) {
            throw new DiningTableStatusAlreadyChangedException();
        }
    }

    @Override
    public void deleteDiningTable(Long id) {
        DiningTableEntity diningTableEntity = diningTableRepository.findById(id)
                .orElseThrow(DiningTableNotFoundException::new);

        this.checkExistingStatus(diningTableEntity.getStatus(), DiningTableStatus.DELETED);
            diningTableEntity.delete();
            diningTableRepository.save(diningTableEntity);

    }

    private void checkExistingStatus(DiningTableStatus currentStatus, DiningTableStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new DiningTableStatusAlreadyChangedException();
        }
    }
}
