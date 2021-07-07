package com.epam.esm.service.exceptions;

/**
 * Custom exception. Is thrown when maloperations performed.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ServiceException extends ServiceLayerException {

    public ServiceException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ServiceException(Exception e, int errorCode) {
        super(e, errorCode);
    }
}
