package com.epam.esm.service.exceptions;

/**
 * Validation exception. Is thrown when entity object is invalid
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
