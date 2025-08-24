package org.violet.restaurantmanagement.payment.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaAlreadyExistException;

import java.io.Serial;

public class OrderItemAlreadyPaidException extends RmaAlreadyExistException {

    @Serial
    private static final long serialVersionUID = 5287585682751614874L;

    public OrderItemAlreadyPaidException() {
        super("Order item already paid");
    }
}
