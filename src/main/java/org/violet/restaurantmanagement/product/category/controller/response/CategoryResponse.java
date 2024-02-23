package org.violet.restaurantmanagement.product.category.controller.response;

import org.violet.restaurantmanagement.product.category.model.enums.CategoryStatus;

import java.time.LocalDateTime;


public record CategoryResponse(

        Long id,
        String name,
        CategoryStatus status,
        LocalDateTime createdAt
) {
}
