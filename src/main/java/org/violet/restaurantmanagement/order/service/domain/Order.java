package org.violet.restaurantmanagement.order.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.order.model.OrderStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Order {

    private String id;
    private String mergeId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class OrderBuilder {
        private OrderBuilder() {
        }

        public OrderBuilder price(BigDecimal totalAmount) {
            this.totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
            return this;
        }
    }
}
