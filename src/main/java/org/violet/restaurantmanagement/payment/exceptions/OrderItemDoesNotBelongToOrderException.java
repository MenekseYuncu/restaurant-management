package org.violet.restaurantmanagement.payment.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaInvalidException;

import java.io.Serial;

public class OrderItemDoesNotBelongToOrderException extends RmaInvalidException {

    @Serial
    private static final long serialVersionUID = -502654520550239896L;

    public OrderItemDoesNotBelongToOrderException(String orderItemId, String orderId) {
        super("Order item does not belong to order. itemId=" + orderItemId + ", orderId=" + orderId);
    }
}
