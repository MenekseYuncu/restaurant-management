package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaInvalidException;

import java.io.Serial;

public class OrderDeletionException extends RmaInvalidException {

    @Serial
    private static final long serialVersionUID = 6264796992102923406L;

    public OrderDeletionException() {
        super("An unexpected error occurred during the order deletion process.");
    }
}
