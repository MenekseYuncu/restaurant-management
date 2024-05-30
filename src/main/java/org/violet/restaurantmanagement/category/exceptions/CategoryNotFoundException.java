package org.violet.restaurantmanagement.category.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class CategoryNotFoundException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = 1721232167659910474L;

    public CategoryNotFoundException() {
        super("Category does not exist");
    }
}
