package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaInvalidException;

import java.io.Serial;

public class InvalidItemQuantityException extends RmaInvalidException {

    @Serial
    private static final long serialVersionUID = -4861374131790147477L;

    public InvalidItemQuantityException() {
        super("Product quantity must be greater than zero.");
    }
}
