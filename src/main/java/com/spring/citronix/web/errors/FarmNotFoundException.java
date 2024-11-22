package com.spring.citronix.web.errors;

import java.util.UUID;

public class FarmNotFoundException extends RuntimeException {

    public FarmNotFoundException(UUID id) {
        super("Farm with ID " + id + " not found.");
    }
}
