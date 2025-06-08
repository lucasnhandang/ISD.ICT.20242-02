package com.hustict.aims.exception;

public class ProductOperationException extends Exception {
    public ProductOperationException(String message) {
        super(message);
    }

    public ProductOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}