package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaAlreadyExistException;

import java.io.Serial;

public class OrderUpdateNotAllowedException extends RmaAlreadyExistException {

    @Serial
    private static final long serialVersionUID = 8041014675615543541L;


    public OrderUpdateNotAllowedException() {
        super("Order update not allowed: status is COMPLETED or CANCELED.");
    }
}
