package org.violet.restaurantmanagement.dining_tables.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableUpdateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableListRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableListCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = DiningTableController.class)
class DiningTableControllerTest extends RmaControllerTest {

    @MockBean
    private DiningTableService diningTableService;
    private static final DiningTableCreateRequestToCommandMapper diningTableCreateRequestToCommandMapper = DiningTableCreateRequestToCommandMapper.INSTANCE;
    private static final DiningTableUpdateRequestToCommandMapper diningTableUpdateRequestToCommandMapper = DiningTableUpdateRequestToCommandMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/dining-table";


    @Test
    void givenValidDiningTableListRequest_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest.DiningTableFilter mockDiningTableFilter = DiningTableListRequest.DiningTableFilter.builder()
                .size(2)
                .statuses(Collections.singleton(DiningTableStatus.VACANT))
                .build();
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .desc()
                                .property("id")
                                .build()
                )
                .filter(mockDiningTableFilter)
                .build();

        // When
        List<DiningTable> mockTables = new ArrayList<>();
        mockTables.add(
                DiningTable.builder()
                        .id(1L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(2L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(3L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );

        RmaPage<DiningTable> rmaPage = RmaPage.<DiningTable>builder()
                .content(mockTables)
                .pageNumber(mockDiningTableListRequest.getPagination().getPageNumber())
                .pageSize(mockTables.size())
                .totalPageCount(mockDiningTableListRequest.getPagination().getPageNumber())
                .totalElementCount(mockTables.size())
                .sortedBy(mockDiningTableListRequest.getSorting())
                .filteredBy(mockDiningTableFilter)
                .build();

        Mockito.when(diningTableService.getAllDiningTables(Mockito.any(DiningTableListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.size")
                        .value(mockDiningTableFilter.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1))
                .getAllDiningTables(Mockito.any(DiningTableListCommand.class));
    }

    @Test
    void givenValidDiningTableListRequestWithoutFilter_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .desc()
                                .property("id")
                                .build()
                )
                .build();

        // When
        List<DiningTable> mockTables = new ArrayList<>();
        mockTables.add(
                DiningTable.builder()
                        .id(1L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.OCCUPIED)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(2L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(3L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.RESERVED)
                        .size(2)
                        .build()
        );

        RmaPage<DiningTable> rmaPage = RmaPage.<DiningTable>builder()
                .content(mockTables)
                .pageNumber(mockDiningTableListRequest.getPagination().getPageNumber())
                .pageSize(mockTables.size())
                .totalPageCount(mockDiningTableListRequest.getPagination().getPageNumber())
                .totalElementCount(mockTables.size())
                .sortedBy(mockDiningTableListRequest.getSorting())
                .build();

        Mockito.when(diningTableService.getAllDiningTables(Mockito.any(DiningTableListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1))
                .getAllDiningTables(Mockito.any(DiningTableListCommand.class));
    }

    @Test
    void givenValidDiningTableListRequestWithoutSorting_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest.DiningTableFilter mockDiningTableFilter = DiningTableListRequest.DiningTableFilter.builder()
                .size(2)
                .build();
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .filter(mockDiningTableFilter)
                .build();

        // When
        List<DiningTable> mockTables = new ArrayList<>();
        mockTables.add(
                DiningTable.builder()
                        .id(1L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.OCCUPIED)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(2L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(3L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.RESERVED)
                        .size(2)
                        .build()
        );

        RmaPage<DiningTable> rmaPage = RmaPage.<DiningTable>builder()
                .content(mockTables)
                .pageNumber(mockDiningTableListRequest.getPagination().getPageNumber())
                .pageSize(mockTables.size())
                .totalPageCount(mockDiningTableListRequest.getPagination().getPageNumber())
                .totalElementCount(mockTables.size())
                .filteredBy(mockDiningTableFilter)
                .build();

        Mockito.when(diningTableService.getAllDiningTables(Mockito.any(DiningTableListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.size")
                        .value(mockDiningTableFilter.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1))
                .getAllDiningTables(Mockito.any(DiningTableListCommand.class));
    }

    @Test
    void givenValidDiningTableListRequestWithoutFilterAndSorting_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .build();

        // When
        List<DiningTable> mockTables = new ArrayList<>();
        mockTables.add(
                DiningTable.builder()
                        .id(1L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.OCCUPIED)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(2L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.VACANT)
                        .size(2)
                        .build()
        );
        mockTables.add(
                DiningTable.builder()
                        .id(3L)
                        .mergeId(String.valueOf(UUID.randomUUID()))
                        .status(DiningTableStatus.RESERVED)
                        .size(2)
                        .build()
        );

        RmaPage<DiningTable> rmaPage = RmaPage.<DiningTable>builder()
                .content(mockTables)
                .pageNumber(mockDiningTableListRequest.getPagination().getPageNumber())
                .pageSize(mockTables.size())
                .totalPageCount(mockDiningTableListRequest.getPagination().getPageNumber())
                .totalElementCount(mockTables.size())
                .build();

        Mockito.when(diningTableService.getAllDiningTables(Mockito.any(DiningTableListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1))
                .getAllDiningTables(Mockito.any(DiningTableListCommand.class));
    }

    @Test
    void givenInvalidDiningTableListRequest_whenTablesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableListRequest givenRequest = DiningTableListRequest.builder().build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(givenRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenInvalidNegativePageSize_whenDiningTablesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(-1)
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenInvalidNegativePageNumber_whenDiningTablesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(-1)
                                .pageSize(1)
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenValidDiningTableId_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        Long diningTableId = 1L;

        // When
        DiningTable diningTable = DiningTable.builder()
                .id(diningTableId)
                .mergeId(String.valueOf(UUID.randomUUID()))
                .status(DiningTableStatus.OCCUPIED)
                .size(2)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(diningTableService.getDiningTableById(diningTableId)).thenReturn(diningTable);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.size")
                        .value(diningTable.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1))
                .getDiningTableById(diningTableId);
    }

    @Test
    void givenInvalidNegativeDiningTableId_whenDiningTableNotFound_thenReturnBadRequest() throws Exception {
        //Given
        Long diningTableId = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(diningTableService)
                .getDiningTableById(diningTableId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenInvalidDiningTableId_whenDiningTableNotFound_thenReturnNotFound() throws Exception {
        // Given
        Long diningTableId = 999L;

        // When
        Mockito.doThrow(DiningTableNotFoundException.class)
                .when(diningTableService)
                .getDiningTableById(diningTableId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1)).getDiningTableById(diningTableId);
    }

    @Test
    void givenValidDiningTableCreateRequest_whenCreateDiningTable_thenReturnsSuccess() throws Exception {
        // Given
        DiningTableCreateRequest diningTableRequest = new DiningTableCreateRequest(
                1,
                5
        );

        // When
        DiningTableCreateCommand createCommand = diningTableCreateRequestToCommandMapper.map(diningTableRequest);
        Mockito.doNothing().when(diningTableService).createDiningTables(Mockito.any(DiningTableCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(diningTableService, Mockito.times(1)).createDiningTables(createCommand);
    }

    @Test
    void givenInvalidCreateRequest_whenCreateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableCreateRequest mockDiningTableRequest = new DiningTableCreateRequest(
                null,
                null
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenInvalidsMaxSize_whenCreateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableCreateRequest mockDiningTableRequest = new DiningTableCreateRequest(
                1,
                11
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenNegativeTableSize_whenCreateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        DiningTableCreateRequest mockDiningTableRequest = new DiningTableCreateRequest(
                1,
                -4
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenValidDiningTableUpdateRequest_whenUpdateDiningTable_thenReturnSuccess() throws Exception {
        //Given
        Long tableId = 1L;

        // When
        DiningTableUpdateRequest updateRequest = new DiningTableUpdateRequest(
                DiningTableStatus.OCCUPIED,
                5
        );

        DiningTableUpdateCommand updateCommand = diningTableUpdateRequestToCommandMapper.map(updateRequest);
        Mockito.doNothing().when(diningTableService).updateDiningTable(tableId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateCommand)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(diningTableService).updateDiningTable(tableId, updateCommand);
    }

    @Test
    void givenInvalidInput_whenUpdateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        Long tableId = 1L;
        DiningTableUpdateRequest updateRequest = new DiningTableUpdateRequest(
                null,
                5
        );

        //When
        DiningTableUpdateCommand updateCommand = diningTableUpdateRequestToCommandMapper.map(updateRequest);
        Mockito.doThrow(new ProductNotFoundException()).when(diningTableService)
                .updateDiningTable(tableId, updateCommand);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenInvalidsMaxSize_whenUpdateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        Long tableId = 1L;
        DiningTableUpdateRequest mockDiningTableRequest = new DiningTableUpdateRequest(
                DiningTableStatus.OCCUPIED,
                11
        );

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenNegativeTableSize_whenUpdateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        Long tableId = 1L;
        DiningTableUpdateRequest mockDiningTableRequest = new DiningTableUpdateRequest(
                DiningTableStatus.OCCUPIED,
                -4
        );

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenDiningTableIdEmpty_whenUpdateDiningTable_thenException() throws Exception {
        // Given
        Long tableId = null;

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenInvalidId_whenUpdateDiningTable_thenReturnBadRequest() throws Exception {
        //Given
        Long tableId = 999L;

        // When
        DiningTableUpdateRequest updateRequest = new DiningTableUpdateRequest(
                DiningTableStatus.OCCUPIED,
                5
        );

        DiningTableUpdateCommand updateCommand = diningTableUpdateRequestToCommandMapper.map(updateRequest);
        Mockito.doThrow(DiningTableNotFoundException.class)
                .when(diningTableService)
                .updateDiningTable(tableId, updateCommand);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }

    @Test
    void givenValidDeletedDiningTableId_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        Long diningTableId = 1L;

        // When
        Mockito.doNothing().when(diningTableService).deleteDiningTable(diningTableId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(diningTableService).deleteDiningTable(diningTableId);
    }

    @Test
    void givenInvalidDeleteId_whenDeleteDiningTableNotFound_thenReturnCategoryNotFound() throws Exception {
        // Given
        Long diningTableId = 100L;

        // When
        Mockito.doThrow(DiningTableNotFoundException.class)
                .when(diningTableService)
                .deleteDiningTable(diningTableId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(diningTableService).deleteDiningTable(diningTableId);
    }

    @Test
    void givenInvalidNegativeId_whenDeleteDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        Long diningTable = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(diningTableService)
                .deleteDiningTable(diningTable);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", diningTable))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }
}