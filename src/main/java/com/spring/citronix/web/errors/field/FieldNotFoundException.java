package com.spring.citronix.web.errors.field;

import java.util.UUID;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(UUID id) {
        super("Field with ID " + id + " not found.");
    }
}