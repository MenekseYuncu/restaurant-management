package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;

public record DiningTableSplitRequest(

        @NotNull
        String mergeId
) {
}
