package com.hustict.aims.exception;
import java.util.List;

public class CartAvailabilityException extends RuntimeException {
    private final List<String> errors;

    public CartAvailabilityException(List<String> errors) {
        super("Some products are not available in the required quantities.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}