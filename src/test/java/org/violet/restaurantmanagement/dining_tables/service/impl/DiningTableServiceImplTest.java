package org.violet.restaurantmanagement.dining_tables.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class DiningTableServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private DiningTableRepository diningTableRepository;

    @InjectMocks
    private DiningTableServiceImpl diningTableService;

    private static final DomainToDiningTableEntityMapper domainToDiningTableEntityMapper = DomainToDiningTableEntityMapper.INSTANCE;
    private static final DiningTableUpdateCommandToDomainMapper diningTableUpdateCommandToDomainMapper = DiningTableUpdateCommandToDomainMapper.INSTANCE;

    @Test
    void givenDiningTablesExists_whenCreateDiningTable_thenDiningTablesEntitySaved() {
        // Given
        DiningTableCreateCommand createCommand = new DiningTableCreateCommand(
                3,
                4
        );

        List<DiningTableEntity> savedEntities = new ArrayList<>();
        Mockito.when(diningTableRepository.saveAll(Mockito.anyList()))
                .thenReturn(savedEntities);

        // When
        diningTableService.createDiningTables(createCommand);

        // Then
        Mockito.verify(diningTableRepository, Mockito.times(1))
                .saveAll(ArgumentMatchers.anyList());
    }

    @Test
    void givenNullCreateCommand_whenCreateDiningTable_thenThrowNullPointerException() {
        // Given
        DiningTableCreateCommand createCommand = new DiningTableCreateCommand(
                null,
                null
        );

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> diningTableService.createDiningTables(createCommand));

        Mockito.verifyNoInteractions(diningTableRepository);
    }

    @Test
    void givenDiningTableExists_whenUpdateDiningTable_thenReturnSuccess() {
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
        Mockito.verify(diningTableRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(DiningTableEntity.class));
    }

    @Test
    void givenDiningTableIdDoesNotExists_whenUpdateDiningTable_thenThrowDiningTableNotFoundException() {
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
    void givenNullUpdateCommand_whenUpdateDiningTable_thenThrowException() {
        // Given
        Long tableId = 1L;
        DiningTableUpdateCommand updateCommand = null;

        // Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.updateDiningTable(tableId, updateCommand));

        Mockito.verify(diningTableRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(DiningTableEntity.class));
    }

    @Test
    void givenTableIdIsNegative_whenUpdateDiningTable_thenThrowException() {
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
        Mockito.verify(diningTableRepository, Mockito.never()).save(ArgumentMatchers.any());
    }
}