package org.violet.restaurantmanagement.payment.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaAlreadyExistException;

import java.io.Serial;

public class PaymentAlreadyCompletedException extends RmaAlreadyExistException {
    @Serial
    private static final long serialVersionUID = -5727832888228052624L;

    public PaymentAlreadyCompletedException(String orderId) {
        super("Payment already completed for order: " + orderId);
    }
}
