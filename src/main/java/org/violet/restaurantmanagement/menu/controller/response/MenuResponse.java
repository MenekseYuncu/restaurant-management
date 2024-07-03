package org.violet.restaurantmanagement.menu.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class MenuResponse {


    private Product product;

    private Category category;


    @Getter
    @Builder
    public static class Category {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class Product {
        private String id;

        private String name;

        private String ingredient;

        private BigDecimal price;

        private String currency;

        private ProductStatus status;

        private Integer extent;

        private ExtentType extentType;
    }

}