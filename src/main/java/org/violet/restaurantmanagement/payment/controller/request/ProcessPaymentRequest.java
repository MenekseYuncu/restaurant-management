package org.violet.restaurantmanagement.payment.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record ProcessPaymentRequest(
        List<OrderItemDto> orderItems

) {
    @Getter
    @Setter
    public static class OrderItemDto {
        private String orderItemId;
    }
}
