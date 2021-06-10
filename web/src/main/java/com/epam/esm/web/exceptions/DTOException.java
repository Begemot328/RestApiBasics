package com.epam.esm.web.exceptions;

/**
 * Exception DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class DTOException extends RuntimeException {
    /**
     * Constructor.
     *
     * @param e Exception to wrap.
     */
    public DTOException(Exception e) {
        super(e);
    }
}
