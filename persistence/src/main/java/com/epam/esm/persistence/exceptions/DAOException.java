package com.epam.esm.persistence.exceptions;

import com.epam.esm.model.exceptions.BaseException;

public class DAOException extends BaseException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }

    public DAOException(Exception e) {
        super(e);
    }
}
