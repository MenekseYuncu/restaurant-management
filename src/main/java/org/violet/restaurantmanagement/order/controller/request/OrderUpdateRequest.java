package org.violet.restaurantmanagement.order.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderUpdateRequest(

        List<@Valid ProductItem> products
) {
    public record ProductItem(
            @NotNull
            String id,

            @NotNull @Min(1)
            Integer quantity
    ) {
    }
}

