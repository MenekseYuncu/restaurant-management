package org.violet.restaurantmanagement.product.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductListResponse(

        String id,
        Long categoryId,
        String name,
        String ingredient,
        BigDecimal price,
        ProductStatus status,
        Integer extent,
        ExtentType extentType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public String getPrice() {
        return String.format("%.2f", price);
    }
}
