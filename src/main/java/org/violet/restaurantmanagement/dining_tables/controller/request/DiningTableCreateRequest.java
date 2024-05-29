package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record DiningTableCreateRequest(

        Integer count,

        @NotNull
        @Range(min = 0, max = 10)
        Integer size
) {
}
