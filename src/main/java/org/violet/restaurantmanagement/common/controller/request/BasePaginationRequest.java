package org.violet.restaurantmanagement.common.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record BasePaginationRequest(
        @NotNull
        @Min(1)
        @Max(Integer.MAX_VALUE)
        int page,

        @NotNull
        @Min(1)
        @Max(Integer.MAX_VALUE)
        int size
) {
}
