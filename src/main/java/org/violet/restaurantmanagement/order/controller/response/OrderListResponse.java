package org.violet.restaurantmanagement.order.controller.response;

import lombok.*;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;
import org.violet.restaurantmanagement.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderListResponse(
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
        private OrderItemStatus status;
        private int quantity;
        private BigDecimal price;
    }
}