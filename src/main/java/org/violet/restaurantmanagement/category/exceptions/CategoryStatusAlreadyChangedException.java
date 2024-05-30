package org.violet.restaurantmanagement.category.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaStatusAlreadyChangedException;

import java.io.Serial;

public class CategoryStatusAlreadyChangedException extends RmaStatusAlreadyChangedException {

    @Serial
    private static final long serialVersionUID = 3211391362591922997L;

    public CategoryStatusAlreadyChangedException() {
        super("Category status already changed");
    }
}
