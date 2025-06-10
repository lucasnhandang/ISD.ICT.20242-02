package com.hustict.aims.exception;

import java.util.List;

public class DeliveryFormValidationException extends RuntimeException {
    private final List<String> errors;

    public DeliveryFormValidationException(List<String> errors) {
        super("Delivery form validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}