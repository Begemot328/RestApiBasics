package com.epam.esm.persistence.exceptions;


public class DAOSQLException extends Exception {

    /**
     * Constructor
     *
     * @param message the message
     */
    public DAOSQLException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public DAOSQLException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public DAOSQLException(Exception e) {
        super(e);
    }
}
