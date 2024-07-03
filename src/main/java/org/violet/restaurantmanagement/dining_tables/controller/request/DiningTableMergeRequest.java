package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DiningTableMergeRequest(

        @NotEmpty
        List<Long> tableIds
) {
}
