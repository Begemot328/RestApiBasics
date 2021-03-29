package com.epam.esm.persistence.exceptions;


public class DAOException extends Exception {

    /**
     * Constructor
     *
     * @param message the message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public DAOException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public DAOException(Exception e) {
        super(e);
    }
}
