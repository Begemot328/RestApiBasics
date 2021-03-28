package com.epam.esm.model.exceptions;

public class ProjectException extends Exception{

    /**
     * Constructor
     *
     * @param message the message
     */
    public ProjectException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public ProjectException(String message, Exception e) {
        super(message + e.getLocalizedMessage());
        this.setStackTrace (e.getStackTrace());
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public ProjectException(Exception e) {
        super(e.getClass().getSimpleName() + ": " +  e.getLocalizedMessage());
        this.setStackTrace (e.getStackTrace());
    }
}
