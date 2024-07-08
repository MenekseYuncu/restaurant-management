package org.violet.restaurantmanagement.dining_tables.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.common.controller.response.RmaPageResponse;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableListRequestToListCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableSplitRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableToDiningTableResponseMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableToMergeRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.mapper.DiningTableUpdateRequestToCommandMapper;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableCreateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableListRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableMergeRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableSplitRequest;
import org.violet.restaurantmanagement.dining_tables.controller.request.DiningTableUpdateRequest;
import org.violet.restaurantmanagement.dining_tables.controller.response.DiningTableResponse;
import org.violet.restaurantmanagement.dining_tables.service.DiningTableService;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableMergeCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableSplitCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/dining-table")
class DiningTableController {

    private final DiningTableService diningTableService;
    private static final DiningTableCreateRequestToCommandMapper diningTableCreateRequestToCommandMapper = DiningTableCreateRequestToCommandMapper.INSTANCE;
    private static final DiningTableUpdateRequestToCommandMapper diningTableUpdateRequestToCommandMapper = DiningTableUpdateRequestToCommandMapper.INSTANCE;
    private static final DiningTableToDiningTableResponseMapper diningTableToResponseMapper = DiningTableToDiningTableResponseMapper.INSTANCE;
    private static final DiningTableListRequestToListCommandMapper diningTableListRequestToListCommandMapper = DiningTableListRequestToListCommandMapper.INSTANCE;
    private static final DiningTableToMergeRequestToCommandMapper diningTableMergeRequestToCommandMapper = DiningTableToMergeRequestToCommandMapper.INSTANCE;
    private static final DiningTableSplitRequestToCommandMapper diningTableSplitRequestToCommandMapper = DiningTableSplitRequestToCommandMapper.INSTANCE;


    @PostMapping("/tables")
    public BaseResponse<RmaPageResponse<DiningTableResponse>> getAllCategories(
            @Valid @RequestBody DiningTableListRequest diningTableListRequest
    ) {

        RmaPage<DiningTable> diningTableRmaPage = diningTableService.getAllDiningTables(
                diningTableListRequestToListCommandMapper.map(diningTableListRequest)
        );

        RmaPageResponse<DiningTableResponse> diningTableResponsePage = RmaPageResponse.<DiningTableResponse>builder()
                .content(diningTableToResponseMapper.map(diningTableRmaPage.getContent()))
                .page(diningTableRmaPage)
                .build();
        return BaseResponse.successOf(diningTableResponsePage);
    }

    @GetMapping("/{id}")
    public BaseResponse<DiningTableResponse> getDiningTableById(
            @PathVariable @Positive Long id) {
        DiningTable diningTable = diningTableService.getDiningTableById(id);
        DiningTableResponse diningTableResponse = diningTableToResponseMapper.map(diningTable);
        return BaseResponse.successOf(diningTableResponse);
    }

    @PostMapping
    public BaseResponse<Void> createDiningTables(
            @Valid @RequestBody DiningTableCreateRequest requests
    ) {
        DiningTableCreateCommand createCommands = diningTableCreateRequestToCommandMapper.map(requests);
        diningTableService.createDiningTables(createCommands);
        return BaseResponse.SUCCESS;
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateDiningTable(
            @Positive @PathVariable Long id,
            @Valid @RequestBody DiningTableUpdateRequest request
    ) {
        DiningTableUpdateCommand command = diningTableUpdateRequestToCommandMapper.map(request);
        diningTableService.updateDiningTable(id, command);
        return BaseResponse.SUCCESS;
    }

    @PostMapping("/merge")
    public BaseResponse<Void> mergeDiningTables(
            @Valid @RequestBody DiningTableMergeRequest diningTableMergeRequest) {

        DiningTableMergeCommand mergeCommand = diningTableMergeRequestToCommandMapper.map(diningTableMergeRequest);
        diningTableService.mergeDiningTables(mergeCommand);

        return BaseResponse.SUCCESS;
    }

    @PostMapping("/split")
    public BaseResponse<Void> splitDiningTables(
            @Valid @RequestBody DiningTableSplitRequest splitRequest
    ) {
        DiningTableSplitCommand splitCommand = diningTableSplitRequestToCommandMapper.map(splitRequest);
        diningTableService.splitDiningTables(splitCommand);

        return BaseResponse.SUCCESS;
    }


    @PutMapping("/{id}/vacant")
    public BaseResponse<Void> changeStatusToVacant(@PathVariable Long id) {
        diningTableService.changeStatusToVacant(id);
        return BaseResponse.SUCCESS;
    }

    @PutMapping("/{id}/occupied")
    public BaseResponse<Void> changeStatusToOccupied(@PathVariable Long id) {
        diningTableService.changeStatusToOccupied(id);
        return BaseResponse.SUCCESS;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deletedCategory(
            @PathVariable @Positive Long id
    ) {
        diningTableService.deleteDiningTable(id);
        return BaseResponse.SUCCESS;
    }
}
