package org.violet.restaurantmanagement.order.service.command;

import org.violet.restaurantmanagement.menu.service.domain.Menu;

import java.util.List;

public record OrderCreateCommand(
        String mergeId,
        List<Menu.Product> products,
        Integer quantity
) {
}
