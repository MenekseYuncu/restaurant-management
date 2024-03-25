package org.violet.restaurantmanagement.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.violet.restaurantmanagement.common.controller.response.ErrorResponse;
import org.violet.restaurantmanagement.product.exceptions.CategoryAlreadyExistsException;
import org.violet.restaurantmanagement.product.exceptions.CategoryNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handleCategoryNotFoundException(CategoryNotFoundException exception) {
        return ErrorResponse.failureOf(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ErrorResponse handleCategoryAlreadyExistsException(CategoryAlreadyExistsException exception) {
        return ErrorResponse.failureOf(HttpStatus.CONFLICT, exception.getMessage());
    }
}