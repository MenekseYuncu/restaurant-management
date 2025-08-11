package org.violet.restaurantmanagement.order.service.command;

import java.util.List;

public record OrderUpdateCommand(

        List<ProductItem> products

) {
    public record ProductItem(
            String id,
            Integer quantity
    ) implements ProductLine {

    }
}
