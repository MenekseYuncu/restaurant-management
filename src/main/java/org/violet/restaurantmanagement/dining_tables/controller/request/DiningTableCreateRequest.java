package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record DiningTableCreateRequest(

        int numberOfTables,

        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        int size
) {
}
