package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record DiningTableCreateRequest(

        Integer numberOfTables,

        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        Integer size
) {
}
