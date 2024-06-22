package org.violet.restaurantmanagement.menu.controller.response;

import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.common.model.enums.RmaCurrency;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;

public record MenuListResponse(

        String id,

        String name,

        String ingredient,

        BigDecimal price,

        RmaCurrency currency,

        ProductStatus status,

        Integer extent,

        ExtentType extentType,

        Category category

) {
    @Getter
    @Setter
    public static class Category {
        private Long id;
        private String name;
    }
}
