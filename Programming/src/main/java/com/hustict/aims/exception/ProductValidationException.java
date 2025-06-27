package com.hustict.aims.exception;

import java.util.List;

public class ProductValidationException extends RuntimeException {  
    private final List<String> validationErrors;

    public ProductValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public ProductValidationException(String message) {
        super(message);
        this.validationErrors = List.of(message);
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}