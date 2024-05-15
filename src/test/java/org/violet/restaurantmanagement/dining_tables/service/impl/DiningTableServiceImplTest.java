package org.violet.restaurantmanagement.dining_tables.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;
import org.violet.restaurantmanagement.util.RmaServiceTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DiningTableServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private DiningTableRepository diningTableRepository;

    @InjectMocks
    private DiningTableServiceImpl diningTableService;

    private static final DomainToDiningTableEntityMapper domainToDiningTableEntityMapper = DomainToDiningTableEntityMapper.INSTANCE;
    private static final DiningTableUpdateCommandToDomainMapper diningTableUpdateCommandToDomainMapper = DiningTableUpdateCommandToDomainMapper.INSTANCE;

    @Test
    void givenCreateDiningTable_whenDiningTablesExists_thenDiningTablesEntitySaved() {
        // Given
        DiningTableCreateCommand createCommand = new DiningTableCreateCommand(3, 4);

        List<DiningTableEntity> savedEntities = new ArrayList<>();
        Mockito.when(diningTableRepository.saveAll(Mockito.anyList()))
                .then(invocation -> {
                    List<DiningTableEntity> entities = invocation.getArgument(0);
                    savedEntities.addAll(entities);
                    return entities;
                });

        // When
        diningTableService.createDiningTables(createCommand);

        // Then
        Assertions.assertEquals(3, savedEntities.size());

        for (DiningTableEntity entity : savedEntities) {
            Assertions.assertNotNull(entity.getMergeId());
            Assertions.assertEquals(DiningTableStatus.VACANT, entity.getStatus());
            Assertions.assertEquals(4, entity.getSize());
        }
    }

    @Test
    void givenUpdateDiningTable_whenTableExists_thenUpdateDiningTableEntity() {
        // Given
        Long tableId = 1L;
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                DiningTableStatus.OCCUPIED,
                5
        );
        DiningTable diningTable = diningTableUpdateCommandToDomainMapper.map(updateCommand);

        DiningTableEntity diningTableEntity = domainToDiningTableEntityMapper.map(diningTable);

        // When
        Mockito.when(diningTableRepository.findById(tableId)).thenReturn(Optional.ofNullable(diningTableEntity));

        diningTableService.updateDiningTable(tableId, updateCommand);

        // Then
        Mockito.verify(diningTableRepository, times(1))
                .save(any(DiningTableEntity.class));
    }

    @Test
    void givenUpdateDiningTable_whenDiningTableIdDoesNotExists_thenThrowDiningTableNotFoundException() {
        // Given
        Long tableId = 5L;
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                DiningTableStatus.OCCUPIED,
                2
        );

        // When
        Mockito.when(diningTableRepository.findById(tableId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.updateDiningTable(tableId, updateCommand));
    }

    @Test
    void givenUpdateDiningTable_whenNullUpdateCommand_thenThrowException() {
        // Given
        Long tableId = 1L;
        DiningTableUpdateCommand updateCommand = null;

        // Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.updateDiningTable(tableId, updateCommand));

        Mockito.verify(diningTableRepository, times(0))
                .save(any(DiningTableEntity.class));
    }

    @Test
    void givenUpdateDiningTable_whenTableIdIsNegative_thenThrowException() {
        // Given
        Long tableId = -1L;
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                DiningTableStatus.OCCUPIED,
                5
        );

        // Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.updateDiningTable(tableId, updateCommand));

        // Verify
        Mockito.verify(diningTableRepository, Mockito.never()).save(any());
    }

}