package org.violet.restaurantmanagement.product.controller.response;

import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.time.LocalDateTime;


public record CategoryResponse(

        Long id,
        String name,
        CategoryStatus status,
        LocalDateTime createdAt
) {
}
