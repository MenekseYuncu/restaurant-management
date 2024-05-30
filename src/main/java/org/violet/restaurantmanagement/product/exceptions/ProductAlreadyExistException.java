package org.violet.restaurantmanagement.product.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaAlreadyExistException;

import java.io.Serial;

public class ProductAlreadyExistException extends RmaAlreadyExistException {
    @Serial
    private static final long serialVersionUID = -5245118577668217455L;

    public ProductAlreadyExistException() {
        super("Product Already Exist");
    }
}
