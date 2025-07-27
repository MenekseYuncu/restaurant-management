package org.violet.restaurantmanagement.order.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.violet.restaurantmanagement.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String orderId,
        OrderStatus status,
        List<OrderProductResponse> products,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductResponse {
        private String productId;
        private String productName;
        private int quantity;
        private BigDecimal price;
    }
}