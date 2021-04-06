package com.epam.esm.service.exceptions;

/**
 * Custom exception for Service layer. Mostly wraps spotted {@link com.epam.esm.persistence.exceptions.DAOSQLException}
 *
 * @author Yury Zmushko
 * @version 1.0
 */
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
