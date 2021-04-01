package com.epam.esm.services.exceptions;

public class BadRequestException extends ServiceException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Exception e) {
        super(message, e);
    }

    public BadRequestException(Exception e) {
        super(e);
    }
}
