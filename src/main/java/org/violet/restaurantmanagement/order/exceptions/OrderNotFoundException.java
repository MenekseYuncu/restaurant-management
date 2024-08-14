package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class OrderNotFoundException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = -2930818398340571041L;

    public OrderNotFoundException() {
        super("Order not found");
    }
}
