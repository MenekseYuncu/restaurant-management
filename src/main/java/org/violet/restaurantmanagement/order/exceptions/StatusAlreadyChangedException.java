package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaStatusAlreadyChangedException;

import java.io.Serial;

public class StatusAlreadyChangedException extends RmaStatusAlreadyChangedException {

    @Serial
    private static final long serialVersionUID = -7571696217284721428L;

    public StatusAlreadyChangedException() {
        super("Status already changed");
    }
}
