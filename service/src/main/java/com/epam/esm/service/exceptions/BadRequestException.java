package com.epam.esm.service.exceptions;

/**
 * Custom exception. Is thrown when request parameters are incorrect
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class BadRequestException extends ServiceException {
    /**
     * Constructor
     *
     * @param message the message
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public BadRequestException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public BadRequestException(Exception e) {
        super(e);
    }
}
