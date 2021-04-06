package com.epam.esm.persistence.exceptions;

/**
 * Custom exception for DAO layer. Mostly wraps spotted {@link java.sql.SQLException}
 *
 * @author Yury Zmushko
 * @version 1.0
 */
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
