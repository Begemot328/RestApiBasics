package com.epam.esm.services.exceptions;

import com.epam.esm.model.exceptions.BaseException;

public class ServiceException extends BaseException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception e) {
        super(message, e);
    }

    public ServiceException(Exception e) {
        super(e);
    }
}
