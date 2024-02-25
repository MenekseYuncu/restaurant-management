package org.violet.restaurantmanagement.product.service.command;

import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

public record CategoryCreateCommand(
        String name,
        CategoryStatus status
) {
}
