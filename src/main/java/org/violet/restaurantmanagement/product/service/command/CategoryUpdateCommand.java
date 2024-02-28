package org.violet.restaurantmanagement.product.service.command;

import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

public record CategoryUpdateCommand(
        String name,
        CategoryStatus status
) {
}
