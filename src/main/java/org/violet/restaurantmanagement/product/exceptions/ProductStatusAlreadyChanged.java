package org.violet.restaurantmanagement.product.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaStatusAlreadyChangedException;

import java.io.Serial;

public class ProductStatusAlreadyChanged extends RmaStatusAlreadyChangedException {

    @Serial
    private static final long serialVersionUID = 7118613782500198421L;

    public ProductStatusAlreadyChanged() {
        super("Product status already changed");
    }
}
