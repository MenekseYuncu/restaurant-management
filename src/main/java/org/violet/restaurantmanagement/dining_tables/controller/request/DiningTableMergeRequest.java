package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DiningTableMergeRequest(

        @NotNull
        List<Long> tableIds
) {
}
