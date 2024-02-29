package org.violet.restaurantmanagement.exception;

public class CustomSaveException extends RuntimeException{

    public CustomSaveException(String message){
        super(message);
    }
}
