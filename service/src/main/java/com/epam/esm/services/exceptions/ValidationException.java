package com.epam.esm.services.exceptions;

import com.epam.esm.model.exceptions.BaseException;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message, e);
    }

    public ValidationException(Exception e) {
        super(e);
    }
}
