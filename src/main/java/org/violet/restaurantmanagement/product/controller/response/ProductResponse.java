package org.violet.restaurantmanagement.product.controller.response;

import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(

        String id,
        Category category,
        String name,
        String ingredient,
        BigDecimal price,
        ProductStatus status,
        Integer extent,
        ExtentType extentType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @Getter
    @Setter
    public static class Category {
        private Long id;
        private String name;
        private CategoryStatus status;
    }
}
