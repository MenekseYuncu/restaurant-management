package org.violet.restaurantmanagement.product.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class ParameterNameNotFoundException extends RmaNotFoundException {


    @Serial
    private static final long serialVersionUID = -7804715550001759595L;

    public ParameterNameNotFoundException() {
        super("Parameter does not exist");
    }
}
