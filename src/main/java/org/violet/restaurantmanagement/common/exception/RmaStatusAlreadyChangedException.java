package org.violet.restaurantmanagement.common.exception;

import java.io.Serial;


public class RmaStatusAlreadyChangedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -766040117622969528L;

    public RmaStatusAlreadyChangedException(String message) {
        super(message);
    }
}
