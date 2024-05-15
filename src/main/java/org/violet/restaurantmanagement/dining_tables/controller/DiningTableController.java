package org.violet.restaurantmanagement.dining_tables.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableUpdateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dining-table")
class DiningTableController {

    private final DiningTableService diningTableService;
    private static final DiningTableCreateRequestToCommandMapper diningTableCreateRequestToCommandMapper = DiningTableCreateRequestToCommandMapper.INSTANCE;
    private static final DiningTableUpdateRequestToCommandMapper diningTableUpdateRequestToCommandMapper = DiningTableUpdateRequestToCommandMapper.INSTANCE;

    @PostMapping
    public BaseResponse<Void> createDiningTables(
            @Valid @RequestBody List<DiningTableCreateRequest> requests
    ) {
        List<DiningTableCreateCommand> createCommands = requests.stream()
                .map(diningTableCreateRequestToCommandMapper::map)
                .toList();
        diningTableService.createDiningTables(createCommands);
        return BaseResponse.SUCCESS;
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateDiningTable(
            @PathVariable Long id,
            @Valid @RequestBody DiningTableUpdateRequest request
    ) {
        DiningTableUpdateCommand command = diningTableUpdateRequestToCommandMapper.map(request);
        diningTableService.updateDiningTable(id, command);
        return BaseResponse.SUCCESS;
    }
}
