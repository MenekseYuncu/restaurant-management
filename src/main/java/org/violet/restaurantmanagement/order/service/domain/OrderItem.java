package org.violet.restaurantmanagement.order.service.domain;

import lombok.*;
import org.violet.restaurantmanagement.order.model.OrderItemStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private String id;
    private String productId;
    private int quantity;
    private BigDecimal price;
    private OrderItemStatus status;
    private String orderId;


    public static class OrderItemBuilder {
        private OrderItemBuilder() {
        }

        public OrderItemBuilder price(BigDecimal price) {
            this.price = price.setScale(2, RoundingMode.HALF_UP);
            return this;
        }
    }
}
