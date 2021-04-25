package com.epam.esm.service.exceptions;


public class ServiceLayerException extends Exception {
    private int errorCode;

    public ServiceLayerException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceLayerException(Exception e, int errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
