package com.spring.citronix.web.errors.field;

public class MaxFieldsInFarmException extends RuntimeException {
    public MaxFieldsInFarmException(String message) {
        super(message);
    }
}
