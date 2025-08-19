package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class OrderItemNotFoundException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = -2689242549880691449L;

    public OrderItemNotFoundException() {
        super("Order item not found");
    }
}
