package com.hustict.aims.exception;

import java.util.List;

public class ProductValidationException extends ProductException {  
    private final List<String> validationErrors;

    public ProductValidationException(List<String> validationErrors) {
        super("Product validation failed: " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public ProductValidationException(String message) {
        super(message);
        this.validationErrors = List.of(message);
    }

    @Override
    public String getErrorCode() {
        return "PRODUCT_VALIDATION_FAILED";
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}