package com.epam.esm.service.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceLayerException extends Exception{
    private HttpStatus status;
    private int errorCode;

    public ServiceLayerException(String message, HttpStatus status, int errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public ServiceLayerException(Exception e, HttpStatus status, int errorCode) {
        super(e);
        this.status = status;
        this.errorCode = errorCode;
    }

    public ServiceLayerException(String message, int errorCodeSuffix, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = generateErrorCode(status.value(),errorCodeSuffix);
    }

    public ServiceLayerException(Exception e, int errorCodeSuffix, HttpStatus status) {
        super(e);
        this.status = status;
        this.errorCode = generateErrorCode(status.value(),errorCodeSuffix);
    }

    public HttpStatus getStatus() {
        return status;
    }

    private int generateErrorCode(int httpStatus, int suffix) {
        return httpStatus * 100 + suffix;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
