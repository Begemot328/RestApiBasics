package com.epam.esm.model.exceptions;

public class BaseException extends Exception{

    /**
     * Constructor
     *
     * @param message the message
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public BaseException(String message, Exception e) {
        super(message + e.getLocalizedMessage());
        this.setStackTrace (e.getStackTrace());
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public BaseException(Exception e) {
        super(e.getClass().getSimpleName() + ": " +  e.getLocalizedMessage());
        this.setStackTrace (e.getStackTrace());
    }
}
