package com.spring.citronix.web.errors;

public class InvalidFarmException extends RuntimeException {

    public InvalidFarmException(String message) {
        super(message);
    }
}
