package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaStatusAlreadyChangedException;

import java.io.Serial;

public class DiningTableAlreadySplitException extends RmaStatusAlreadyChangedException {

    @Serial
    private static final long serialVersionUID = 2627725118652769326L;

    public DiningTableAlreadySplitException() {
        super("Dining table already split");
    }
}
