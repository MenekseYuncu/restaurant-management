package org.violet.restaurantmanagement.order.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.violet.restaurantmanagement.order.service.command.ProductLine;

import java.util.List;

public record OrderUpdateRequest(

        List<@Valid ProductItem> products
) {
    public record ProductItem(
            @NotBlank
            String id,

            @NotNull
            @Min(1)
            Integer quantity
    ) implements ProductLine {

    }
}

