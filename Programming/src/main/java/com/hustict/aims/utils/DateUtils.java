package com.hustict.aims.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;

public class DateUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static LocalDate parseDate(Object dateValue, String fieldName) {
        if (dateValue == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        
        try {
            return objectMapper.convertValue(dateValue, LocalDate.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format. Expected: YYYY-MM-DD, got: " + dateValue);
        }
    }

    public static LocalDate parseDateNullable(Object dateValue, String fieldName) {
        if (dateValue == null) {
            return null;
        }
        
        try {
            return objectMapper.convertValue(dateValue, LocalDate.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format. Expected: YYYY-MM-DD, got: " + dateValue);
        }
    }
} 