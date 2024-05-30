package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class DiningTableNotFoundException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = -3498166080908300779L;

    public DiningTableNotFoundException(){
        super("Table not found");
    }
}
