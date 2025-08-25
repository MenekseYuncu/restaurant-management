package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaInvalidException;

import java.io.Serial;

public  class OrderItemsCanNotBeNullException extends RmaInvalidException {

    @Serial
    private static final long serialVersionUID = 4134376313148081694L;

    public OrderItemsCanNotBeNullException() {
        super("Order items cannot be null");
    }
}