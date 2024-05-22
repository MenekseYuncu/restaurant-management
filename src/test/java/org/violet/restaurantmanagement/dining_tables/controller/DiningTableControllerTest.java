package org.violet.restaurantmanagement.dining_tables.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableUpdateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;


class DiningTableControllerTest extends RmaControllerTest implements RmaTestContainer {

    @MockBean
    private DiningTableService diningTableService;
    private static final DiningTableCreateRequestToCommandMapper diningTableCreateRequestToCommandMapper = DiningTableCreateRequestToCommandMapper.INSTANCE;
    private static final DiningTableUpdateRequestToCommandMapper diningTableUpdateRequestToCommandMapper = DiningTableUpdateRequestToCommandMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/dining-table";

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
    void givenInvalidsMaxSize_whenCreateDiningTable_thenReturnBadRequest() throws Exception {
        // Given
        int maxSize = Integer.MAX_VALUE;
        DiningTableCreateRequest mockDiningTableRequest = new DiningTableCreateRequest(
                1,
                maxSize + 1
        );

        // When & Then
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

        // When & Then
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
    void givenDiningTableIdEmpty_whenUpdateDiningTable_thenException() throws Exception {
        // Given
        Long tableId = null;

        // When/Then
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
}