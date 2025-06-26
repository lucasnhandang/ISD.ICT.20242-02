package com.hustict.aims.exception;

import java.time.LocalDateTime;

public class RushOrderException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime expectedDateTime;

    public RushOrderException(String message, String errorCode, LocalDateTime expectedDateTime) {
        super(message);
        this.errorCode = errorCode;
        this.expectedDateTime = expectedDateTime;
    }

    public RushOrderException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.expectedDateTime = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getExpectedDateTime() {
        return expectedDateTime;
    }
} 