package org.violet.restaurantmanagement.product.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductListResponse(

        String id,
        Category category,
        String name,
        String ingredient,
        BigDecimal price,
        String currency,
        ProductStatus status,
        Integer extent,
        ExtentType extentType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @Getter
    @Builder
    public static class Category {
        private Long id;
        private String name;
    }
}
