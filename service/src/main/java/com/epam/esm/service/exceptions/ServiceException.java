package com.epam.esm.service.exceptions;


public class ServiceException extends Exception {

    /**
     * Constructor
     *
     * @param message the message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public ServiceException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ServiceException(Exception e) {
        super(e);
    }
}
