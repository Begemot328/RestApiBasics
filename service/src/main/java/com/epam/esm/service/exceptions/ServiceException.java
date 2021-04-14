package com.epam.esm.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for Service layer. Mostly wraps spotted {@link com.epam.esm.persistence.exceptions.DAOSQLException}
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ServiceException extends ServiceLayerException {


    public ServiceException(String message, HttpStatus status, int errorCode) {
        super(message, status, errorCode);
    }

    public ServiceException(Exception e, HttpStatus status, int errorCode) {
        super(e, status, errorCode);
    }

    public ServiceException(String message, int errorCodeSuffix, HttpStatus status) {
        super(message, errorCodeSuffix, status);
    }

    public ServiceException(Exception e, int errorCodeSuffix, HttpStatus status) {
        super(e, errorCodeSuffix, status);
    }
}
