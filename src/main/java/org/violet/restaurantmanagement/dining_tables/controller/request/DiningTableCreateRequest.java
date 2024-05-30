package org.violet.restaurantmanagement.dining_tables.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record DiningTableCreateRequest(

        @Range(min = 1, max = 50)
        Integer count,

        @NotNull
        @Range(min = 1, max = 10)
        Integer size
) {
}
