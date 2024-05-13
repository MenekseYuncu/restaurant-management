package org.violet.restaurantmanagement.product.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Product {

    private UUID id;
    private Long categoryId;
    private String name;
    private String ingredient;
    private BigDecimal price;
    private ProductStatus status;
    private Integer extent;
    private ExtentType extentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
