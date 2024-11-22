package com.spring.citronix.web.errors;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(String string) {
        super(string);
    }
}