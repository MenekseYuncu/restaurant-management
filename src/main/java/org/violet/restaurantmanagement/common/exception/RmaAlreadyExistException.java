package org.violet.restaurantmanagement.common.exception;

import java.io.Serial;

public class RmaAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8650489690571070800L;

    public RmaAlreadyExistException(String message) {
        super(message);
    }
}
