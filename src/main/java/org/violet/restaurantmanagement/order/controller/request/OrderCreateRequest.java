package org.violet.restaurantmanagement.order.controller.request;

import lombok.Builder;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

import java.util.List;

@Builder
public record OrderCreateRequest(
        String mergeId,

        List<OrderCreateCommand.ProductItem> products
) {
}
