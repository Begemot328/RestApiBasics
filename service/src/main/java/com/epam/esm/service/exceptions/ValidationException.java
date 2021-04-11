package com.epam.esm.service.exceptions;

/**
 * Validation exception. Is thrown when entity object is invalid
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ValidationException extends Exception {
    private int errorCode;

    /**
     * Constructor
     *
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public ValidationException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ValidationException(Exception e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ValidationException(Exception e, int errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public ValidationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
