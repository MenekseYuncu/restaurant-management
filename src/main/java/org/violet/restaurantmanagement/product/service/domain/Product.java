package org.violet.restaurantmanagement.product.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Product {

    private String id;
    private Long categoryId;
    private Category category;
    private String name;
    private String ingredient;
    private BigDecimal price;
    private String currency;
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;
    private Integer extent;
    private ExtentType extentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class ProductBuilder {
        private ProductBuilder() {
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price.setScale(2, RoundingMode.HALF_UP);
            return this;
        }
    }
}
