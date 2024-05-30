package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaStatusAlreadyChangedException;

import java.io.Serial;

public class DiningTableStatusAlreadyChangedException extends RmaStatusAlreadyChangedException {

    @Serial
    private static final long serialVersionUID = -210917142935323024L;

    public DiningTableStatusAlreadyChangedException() {
        super("Status already changed.");
    }
}
