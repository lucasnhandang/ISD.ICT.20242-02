package com.hustict.aims.exception;

import java.util.List;

public class BadSessionDataException extends RuntimeException {
    private final List<String> missingFields;

    public BadSessionDataException(List<String> missingFields) {
        super("Missing or invalid session data");
        this.missingFields = missingFields;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }
}
