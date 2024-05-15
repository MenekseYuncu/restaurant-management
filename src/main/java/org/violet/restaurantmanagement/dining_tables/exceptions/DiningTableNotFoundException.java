package org.violet.restaurantmanagement.dining_tables.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DiningTableNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3498166080908300779L;

    public DiningTableNotFoundException(){
        super("Table not found");
    }
}
