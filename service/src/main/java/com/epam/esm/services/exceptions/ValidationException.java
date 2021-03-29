package com.epam.esm.services.exceptions;


public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message, e);
    }

    public ValidationException(Exception e) {
        super(e);
    }
}
