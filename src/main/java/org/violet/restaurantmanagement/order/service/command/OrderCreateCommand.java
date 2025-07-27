package org.violet.restaurantmanagement.order.service.command;


import java.util.List;

public record OrderCreateCommand(

        String mergeId,
        List<ProductItem> products

) {
    public record ProductItem(
            String productId,
            int quantity
    ) {
    }
}
