package org.violet.restaurantmanagement.payment.service.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record ProcessPaymentCommand(
        List<OrderItemDto> orderItems

) {
    @Getter
    @Setter
    public static class OrderItemDto {
        private String orderItemId;
    }
}