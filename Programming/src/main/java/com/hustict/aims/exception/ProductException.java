package com.hustict.aims.exception;

public abstract class ProductException extends RuntimeException {
    protected ProductException(String message) {
        super(message);
    }

    public abstract String getErrorCode();
} 