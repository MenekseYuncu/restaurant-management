package org.violet.restaurantmanagement.dining_tables.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.dining_tables.exceptions.DiningTableNotFoundException;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.product.exceptions.ProductNotFoundException;
import org.violet.restaurantmanagement.util.RmaControllerTest;
import org.violet.restaurantmanagement.util.RmaTestContainer;

class DiningTableControllerTest extends RmaControllerTest implements RmaTestContainer {

    @MockBean
    private DiningTableService diningTableService;

    private final static String BASE_URL = "/api/v1/dining-table";


    @Test
    void givenUpdateDiningTable_whenValidInput_thenReturnSuccess() throws Exception {
        //Given
        Long tableId = 1L;

        // When
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                DiningTableStatus.OCCUPIED,
                5
        );

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
    void givenUpdateDiningTable_whenInvalidInput_thenReturnBadRequest() throws Exception {
        // Given
        Long tableId = 1L;
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                null,
                1
        );

        //When
        Mockito.doThrow(new ProductNotFoundException()).when(diningTableService)
                .updateDiningTable(tableId, updateCommand);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(diningTableService);
    }


    @Test
    void givenUpdateDiningTable_whenDiningTableIdEmpty_thenException() throws Exception {
        // Given
        Long tableId = null;

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", tableId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenUpdateDiningTable_whenInvalidId_thenReturnBadRequest() throws Exception {
        //Given
        Long tableId = 999L;

        // When
        DiningTableUpdateCommand updateCommand = new DiningTableUpdateCommand(
                DiningTableStatus.OCCUPIED,
                5
        );

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