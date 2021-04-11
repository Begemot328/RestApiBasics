package com.epam.esm.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for Service layer. Mostly wraps spotted {@link com.epam.esm.persistence.exceptions.DAOSQLException}
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ControllerException extends Exception {
    private int errorCode;
    private HttpStatus status;

    /**
     * Constructor
     *
     * @param message the message
     */
    public ControllerException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public ControllerException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ControllerException(Exception e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ControllerException(Exception e, int errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public ControllerException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    /**
     * Constructor
     *
     * @param exception the message
     */
    public ControllerException(Exception exception, HttpStatus status, int errorCode) {
        super(exception);
        this.errorCode = errorCode;
        this.status = status;
    }

    /**
     * Constructor
     *
     * @param exception the message
     */
    public ControllerException(Exception exception, HttpStatus status) {
        super(exception);
        this.errorCode = status.value();
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
