package com.epam.esm.service.exceptions;

/**
 * Validation exception. Is thrown when entity object is invalid
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ValidationException extends ServiceLayerException {

    public ValidationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ValidationException(Exception e, int errorCode) {
        super(e, errorCode);
    }
}
