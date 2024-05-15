package org.violet.restaurantmanagement.dining_tables.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dining-table")
public class DiningTableController {

    private final DiningTableService diningTableService;
    private static final DiningTableCreateRequestToCommandMapper diningTableCreateRequestToCommandMapper = DiningTableCreateRequestToCommandMapper.INSTANCE;


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

}
