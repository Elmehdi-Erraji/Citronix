package com.spring.citronix.web.errors.harvest;

public class SeasonConflictException extends RuntimeException {
    public SeasonConflictException(String message) {
        super(message);
    }
}
