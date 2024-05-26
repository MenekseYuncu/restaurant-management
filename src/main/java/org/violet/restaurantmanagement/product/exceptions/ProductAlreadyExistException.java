package org.violet.restaurantmanagement.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5245118577668217455L;

    public ProductAlreadyExistException() {
        super("Product Already Exist");
    }
}
