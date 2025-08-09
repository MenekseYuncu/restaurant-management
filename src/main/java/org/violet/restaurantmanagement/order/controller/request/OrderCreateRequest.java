package org.violet.restaurantmanagement.order.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(

        @NotNull
        String mergeId,

        List<ProductItem> products
) {
    public record ProductItem(

            @NotNull
            String productId,

            @NotNull
            @Min(1)
            int quantity
    ) {
    }
}
