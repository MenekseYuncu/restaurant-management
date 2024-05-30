package org.violet.restaurantmanagement.common.exception;

import java.io.Serial;

public class RmaNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6191018889083755865L;

    public RmaNotFoundException(String message) {
        super(message);
    }
}
