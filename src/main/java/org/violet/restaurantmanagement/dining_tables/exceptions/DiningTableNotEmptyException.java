package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class DiningTableNotEmptyException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = 599918291160644714L;

    public DiningTableNotEmptyException() {
        super("Dining table not empty");
    }
}
