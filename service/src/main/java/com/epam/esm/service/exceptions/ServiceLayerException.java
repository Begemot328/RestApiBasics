package com.epam.esm.service.exceptions;

/**
 * Parent custom exception.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ServiceLayerException extends Exception {
    private final int errorCode;

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
