package com.epam.esm.service.exceptions;

public class NotFoundException extends ServiceLayerException{
    public NotFoundException(String message, int errorCode) {
        super(message, errorCode);
    }

    public NotFoundException(Exception e, int errorCode) {
        super(e, errorCode);
    }
}
