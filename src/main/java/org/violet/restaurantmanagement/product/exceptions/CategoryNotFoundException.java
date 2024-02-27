package org.violet.restaurantmanagement.product.exceptions;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
