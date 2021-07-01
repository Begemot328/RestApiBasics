package com.epam.esm.service.exceptions;

/**
 * Custom exception. Is thrown when request parameters are incorrect.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class BadRequestException extends ServiceLayerException {

    public BadRequestException(String message, int errorCode) {
        super(message, errorCode);
    }

    public BadRequestException(Exception e, int errorCode) {
        super(e, errorCode);
    }
}
