package com.epam.esm.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception. Is thrown when request parameters are incorrect
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class BadRequestException extends ServiceLayerException {

    public BadRequestException(String message, HttpStatus status, int errorCode) {
        super(message, status, errorCode);
    }

    public BadRequestException(Exception e, HttpStatus status, int errorCode) {
        super(e, status, errorCode);
    }

    public BadRequestException(String message, int errorCodeSuffix, HttpStatus status) {
        super(message, errorCodeSuffix, status);
    }

    public BadRequestException(Exception e, int errorCodeSuffix, HttpStatus status) {
        super(e, errorCodeSuffix, status);
    }
}
