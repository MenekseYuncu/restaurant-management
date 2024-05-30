package org.violet.restaurantmanagement.product.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class ProductNotFoundException extends RmaNotFoundException {

    @Serial
    private static final long serialVersionUID = -2032143531830784052L;

    public ProductNotFoundException() {
        super("Product does not exist");
    }
}
