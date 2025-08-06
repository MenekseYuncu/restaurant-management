package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaInvalidException;

import java.io.Serial;

public class InvalidItemQuantityException extends RmaInvalidException {

    @Serial
    private static final long serialVersionUID = -4650242922247818249L;

    public InvalidItemQuantityException() {
        super("Product quantity can not be less than one.");
    }
}
