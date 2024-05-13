package org.violet.restaurantmanagement.product.service.command;

import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;

public record ProductUpdateCommand(
        String name,
        String ingredient,
        BigDecimal price,
        ProductStatus status,
        Integer extent,
        ExtentType extentType
) {
}
