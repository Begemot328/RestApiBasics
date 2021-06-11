package com.epam.esm.service.exceptions;

/**
 * Custom exception. Is thrown when found no entities.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class NotFoundException extends ServiceLayerException {
    public NotFoundException(String message, int errorCode) {
        super(message, errorCode);
    }

    public NotFoundException(Exception e, int errorCode) {
        super(e, errorCode);
    }
}
