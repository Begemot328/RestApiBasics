package com.epam.esm.services.exceptions;

import com.epam.esm.model.exceptions.ProjectException;

public class ValidationException extends ProjectException {
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
