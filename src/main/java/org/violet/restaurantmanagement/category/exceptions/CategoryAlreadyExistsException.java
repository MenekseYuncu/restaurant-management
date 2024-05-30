package org.violet.restaurantmanagement.category.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaAlreadyExistException;

import java.io.Serial;

public class CategoryAlreadyExistsException extends RmaAlreadyExistException {

    @Serial
    private static final long serialVersionUID = 1721232167659910474L;

    public CategoryAlreadyExistsException(){
        super("Category already exists");
    }
}
