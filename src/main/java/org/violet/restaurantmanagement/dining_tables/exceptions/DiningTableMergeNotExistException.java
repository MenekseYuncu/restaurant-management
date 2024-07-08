package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class DiningTableMergeNotExistException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = -8971651225769720845L;

    public DiningTableMergeNotExistException() {
        super("merge id not exist!");
    }
}
