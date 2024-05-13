package org.violet.restaurantmanagement.category.controller.response;

import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;

import java.time.LocalDateTime;


public record CategoryResponse(
        Long id,
        String name,
        CategoryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
