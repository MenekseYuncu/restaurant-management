package org.violet.restaurantmanagement.dining_tables.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaEndToEndTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.SortingBuilder;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableListRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableMergeRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableSplitRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class DiningTableEndToEndTest extends RmaEndToEndTest implements RmaTestContainer {

    @Autowired
    private DiningTableRepository diningTableRepository;

    private final static String BASE_URL = "/api/v1/dining-table";

    @Test
    void givenValidDiningTableListRequest_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest.DiningTableFilter mockDiningTableFilter = DiningTableListRequest.DiningTableFilter.builder()
                .size(2)
                .build();
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(1)
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

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(mockDiningTableListRequest.getPagination().getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(mockDiningTableListRequest.getPagination().getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.size")
                        .value(mockDiningTableFilter.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidDiningTableListRequestWithoutFilter_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .desc()
                                .property("id")
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(mockDiningTableListRequest.getPagination().getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(mockDiningTableListRequest.getPagination().getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
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
                                .pageSize(1)
                                .build()
                )
                .filter(mockDiningTableFilter)
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(mockDiningTableListRequest.getPagination().getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(mockDiningTableListRequest.getPagination().getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.size")
                        .value(mockDiningTableFilter.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void givenValidDiningTableListRequestWithoutFilterAndSorting_whenDiningTablesFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableListRequest mockDiningTableListRequest = DiningTableListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockDiningTableListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(mockDiningTableListRequest.getPagination().getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(mockDiningTableListRequest.getPagination().getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenGetDiningTableByIdExist_thenReturnDiningTable() throws Exception {
        // Given
        DiningTableEntity diningTable = DiningTableEntity.builder()
                .mergeId(String.valueOf(UUID.randomUUID()))
                .status(DiningTableStatus.VACANT)
                .size(2)
                .build();

        diningTableRepository.save(diningTable);

        DiningTableEntity diningTableEntity = diningTableRepository.findById(diningTable.getId())
                .orElseThrow(DiningTableNotFoundException::new);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", diningTable.getId()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(diningTableEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.mergeId").value(diningTableEntity.getMergeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.size").value(diningTableEntity.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.status").value(diningTableEntity.getStatus()
                        .toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));
    }

    @Test
    void whenCreateDiningTable_thenReturnsSuccess() throws Exception {
        // Given
        DiningTableCreateRequest diningTableRequest = new DiningTableCreateRequest(
                1,
                5
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(diningTableRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenValidUpdateDiningTableId_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        DiningTableEntity diningTableEntity = DiningTableEntity.builder()
                .mergeId(String.valueOf(UUID.randomUUID()))
                .status(DiningTableStatus.VACANT)
                .size(2)
                .build();

        diningTableRepository.save(diningTableEntity);

        DiningTableUpdateRequest updateRequest = new DiningTableUpdateRequest(
                DiningTableStatus.RESERVED,
                3
        );

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", diningTableEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenValidDiningTableMergeRequest_whenMergeDiningTable_thenReturnSuccess() throws Exception {
        //Given
        DiningTableMergeRequest mergeRequest = new DiningTableMergeRequest(
                List.of(4L, 5L)
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/merge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mergeRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenValidSplitRequest_whenSplitDiningTable_thenReturnSuccess() throws Exception {
        // Given
        String mergeId = UUID.randomUUID().toString();
        List<DiningTableEntity> tables = new ArrayList<>();
        tables.add(
                DiningTableEntity.builder()
                        .id(1L)
                        .mergeId(mergeId)
                        .status(DiningTableStatus.OCCUPIED)
                        .size(2)
                        .build()
        );
        tables.add(
                DiningTableEntity.builder()
                        .id(2L)
                        .mergeId(mergeId)
                        .status(DiningTableStatus.OCCUPIED)
                        .size(2)
                        .build()
        );

        diningTableRepository.saveAll(tables);

        DiningTableSplitRequest splitRequest = new DiningTableSplitRequest(mergeId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(STR."\{BASE_URL}/split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(splitRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenChangeStatusToVacant_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        Long tableId = 1L;

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/vacant", tableId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenChangeStatusToOccupied_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        Long tableId = 1L;

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}/occupied", tableId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }

    @Test
    void givenValidDeletedDiningTableId_whenDiningTableFound_thenReturnSuccess() throws Exception {
        // Given
        Long diningTableId = 1L;

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", diningTableId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));
    }
}
