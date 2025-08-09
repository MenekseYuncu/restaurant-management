package org.violet.restaurantmanagement.order.controller.request;

import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

import java.util.List;

public record OrderCreateRequest(
        String mergeId,

        List<OrderCreateCommand.ProductItem> products
) {
}
