package org.violet.restaurantmanagement.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2032143531830784052L;

    public ProductNotFoundException() {
        super("Product does not exist");
    }
}
