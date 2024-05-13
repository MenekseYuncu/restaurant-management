package org.violet.restaurantmanagement.category.service.command;

import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;

public record CategoryUpdateCommand(
        String name,
        CategoryStatus status
) {
}
