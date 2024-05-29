package org.violet.restaurantmanagement.dining_tables.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableEntityToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DiningTableUpdateCommandToDomainMapper;
import org.violet.restaurantmanagement.dining_tables.model.mapper.DomainToDiningTableEntityMapper;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableListCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


class DiningTableServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private DiningTableRepository diningTableRepository;
    @InjectMocks
    private DiningTableServiceImpl diningTableService;

    private static final DomainToDiningTableEntityMapper domainToDiningTableEntityMapper = DomainToDiningTableEntityMapper.INSTANCE;
    private static final DiningTableEntityToDomainMapper diningTableEntityToDomainMapper = DiningTableEntityToDomainMapper.INSTANCE;
    private static final DiningTableUpdateCommandToDomainMapper diningTableUpdateCommandToDomainMapper = DiningTableUpdateCommandToDomainMapper.INSTANCE;


    @Test
    void givenDiningTableListExist_whenGetAllTables_thenReturnDiningTables() {
        // Given
        DiningTableListCommand.DiningTableFilter mocDiningTableFilter = DiningTableListCommand.DiningTableFilter.builder()
                .size(2)
                .build();
        DiningTableListCommand tableListCommand = DiningTableListCommand.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .asc()
                                .property("id")
                                .build()
                )
                .filter(mocDiningTableFilter)
                .build();

        // When
        List<DiningTableEntity> diningTableEntities = new ArrayList<>();
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.OCCUPIED,
                        2
                )
        );
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.RESERVED,
                        2
                )
        );
        Page<DiningTableEntity> diningTableEntityPage = new PageImpl<>(diningTableEntities);

        Mockito.when(diningTableRepository.findAll(
                Mockito.<Specification<DiningTableEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(diningTableEntityPage);

        // Then
        RmaPage<DiningTable> result = diningTableService.getAllDiningTables(tableListCommand);

        // Assertions
        Mockito.verify(diningTableRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getStatus(), diningTableEntities.get(0).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getStatus(), diningTableEntities.get(1).getStatus());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(),
                tableListCommand.getPagination().getPageSize()
        );

        Assertions.assertEquals(result.getFilteredBy(), tableListCommand.getFilter());
    }

    @Test
    void givenDiningTableListExistWithoutSorting_whenGetAllTables_thenReturnDiningTables() {
        // Given
        DiningTableListCommand.DiningTableFilter mocDiningTableFilter = DiningTableListCommand.DiningTableFilter.builder()
                .size(2)
                .build();
        DiningTableListCommand tableListCommand = DiningTableListCommand.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .filter(mocDiningTableFilter)
                .build();

        // When
        List<DiningTableEntity> diningTableEntities = new ArrayList<>();
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.OCCUPIED,
                        2
                )
        );
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.RESERVED,
                        2
                )
        );
        Page<DiningTableEntity> diningTableEntityPage = new PageImpl<>(diningTableEntities);

        Mockito.when(diningTableRepository.findAll(
                Mockito.<Specification<DiningTableEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(diningTableEntityPage);

        // Then
        RmaPage<DiningTable> result = diningTableService.getAllDiningTables(tableListCommand);

        // Assertions
        Mockito.verify(diningTableRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getStatus(), diningTableEntities.get(0).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getStatus(), diningTableEntities.get(1).getStatus());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(),
                tableListCommand.getPagination().getPageSize()
        );

        Assertions.assertEquals(result.getFilteredBy(), tableListCommand.getFilter());
    }

    @Test
    void givenDiningTableListExistWithoutFilter_whenGetAllTables_thenReturnDiningTables() {
        // Given
        DiningTableListCommand tableListCommand = DiningTableListCommand.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .asc()
                                .property("id")
                                .build()
                )
                .build();

        // When
        List<DiningTableEntity> diningTableEntities = new ArrayList<>();
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.OCCUPIED,
                        2
                )
        );
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.RESERVED,
                        2
                )
        );
        Page<DiningTableEntity> diningTableEntityPage = new PageImpl<>(diningTableEntities);

        Mockito.when(diningTableRepository.findAll(
                Mockito.<Specification<DiningTableEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(diningTableEntityPage);

        // Then
        RmaPage<DiningTable> result = diningTableService.getAllDiningTables(tableListCommand);

        // Assertions
        Mockito.verify(diningTableRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getStatus(), diningTableEntities.get(0).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getStatus(), diningTableEntities.get(1).getStatus());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(),
                tableListCommand.getPagination().getPageSize()
        );
    }

    @Test
    void givenDiningTableListExistWithoutFilterAndSorting_whenGetAllTables_thenReturnDiningTables() {
        // Given
        DiningTableListCommand tableListCommand = DiningTableListCommand.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .build();

        // When
        List<DiningTableEntity> diningTableEntities = new ArrayList<>();
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.OCCUPIED,
                        2
                )
        );
        diningTableEntities.add(new DiningTableEntity(
                        1L,
                        String.valueOf(UUID.randomUUID()),
                        DiningTableStatus.RESERVED,
                        2
                )
        );
        Page<DiningTableEntity> diningTableEntityPage = new PageImpl<>(diningTableEntities);

        Mockito.when(diningTableRepository.findAll(
                Mockito.<Specification<DiningTableEntity>>any(),
                Mockito.any(Pageable.class))
        ).thenReturn(diningTableEntityPage);

        // Then
        RmaPage<DiningTable> result = diningTableService.getAllDiningTables(tableListCommand);

        // Assertions
        Mockito.verify(diningTableRepository, Mockito.never()).findAll();

        Assertions.assertEquals(result.getContent().get(0).getStatus(), diningTableEntities.get(0).getStatus());
        Assertions.assertEquals(result.getContent().get(1).getStatus(), diningTableEntities.get(1).getStatus());

        Assertions.assertEquals(2, result.getPageSize());
        Assertions.assertEquals(result.getTotalElementCount(),
                tableListCommand.getPagination().getPageSize()
        );
    }

    @Test
    void givenResultIsNull_whenGetAllDiningTables_thenThrowNullPointerException() {
        // When
        DiningTableListCommand diningTableListCommand = DiningTableListCommand.builder().build();

        // Then
        Assertions.assertThrows(NullPointerException.class,
                () -> diningTableService.getAllDiningTables(diningTableListCommand)
        );
    }

    @Test
    void givenDiningTableExists_whenGetDiningTableById_thenReturnCategory() {
        //Given
        Long diningTableId = 1L;
        DiningTableEntity diningTableEntity = DiningTableEntity.builder()
                .id(diningTableId)
                .mergeId(String.valueOf(UUID.randomUUID()))
                .status(DiningTableStatus.VACANT)
                .size(2)
                .build();


        //When
        Mockito.when(diningTableRepository.findById(diningTableId))
                .thenReturn(Optional.of(diningTableEntity));

        DiningTable mockDiningTable = diningTableEntityToDomainMapper.map(diningTableEntity);

        //Then
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);

        Assertions.assertNotNull(diningTable);
        Assertions.assertEquals(mockDiningTable.getId(), diningTable.getId());
        Assertions.assertEquals(mockDiningTable.getMergeId(), diningTable.getMergeId());
        Assertions.assertEquals(mockDiningTable.getStatus(), diningTable.getStatus());
        Assertions.assertEquals(mockDiningTable.getSize(), diningTable.getSize());
    }

    @Test
    void givenDiningTableDoesNotExist_whenGetDiningTableById_thenThrowCategoryNotFoundException() {
        //Given
        Long diningTableId = 1L;

        //When
        Mockito.when(diningTableRepository.findById(diningTableId)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.getDiningTableById(diningTableId));
    }

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

    @Test
    void givenTableIdNotValid_whenUpdateDiningTable_thenThrowException() {
        // Given
        Long tableId = 999L;
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

    @Test
    void givenDiningTableIdExists_whenSoftDeleteDiningTable_thenReturnSuccess() {
        // Given
        Long diningTableId = 1L;
        DiningTableEntity diningTableEntity = new DiningTableEntity();
        diningTableEntity.delete();

        // When
        Mockito.when(diningTableRepository.findById(diningTableId))
                .thenReturn(Optional.of(diningTableEntity));

        diningTableService.deleteDiningTable(diningTableId);

        // Then
        Mockito.verify(diningTableRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(DiningTableEntity.class));
    }

    @Test
    void givenCategoryIdDoesNotExists_whenSoftDeleteCategory_thenThrowCategoryNotFoundException() {
        //Given
        Long diningTableId = 1L;

        //When
        Mockito.when(diningTableRepository.findById(diningTableId))
                .thenReturn(Optional.empty());

        //Then
        Assertions.assertThrows(DiningTableNotFoundException.class,
                () -> diningTableService.deleteDiningTable(diningTableId));
    }
}