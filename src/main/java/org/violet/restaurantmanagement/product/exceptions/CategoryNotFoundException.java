package org.violet.restaurantmanagement.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1721232167659910474L;

    public CategoryNotFoundException() {
        super("Category does not exist");
    }
}
