package com.spring.citronix.web.errors;

public class SeasonConflictException extends RuntimeException {
    public SeasonConflictException(String message) {
        super(message);
    }
}
