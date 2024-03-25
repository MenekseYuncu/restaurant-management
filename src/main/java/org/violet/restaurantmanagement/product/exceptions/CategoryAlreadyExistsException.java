package org.violet.restaurantmanagement.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1721232167659910474L;

    public CategoryAlreadyExistsException(){
        super("Category already exists");
    }
}
