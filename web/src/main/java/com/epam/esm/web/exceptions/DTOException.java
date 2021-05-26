package com.epam.esm.web.exceptions;

public class DTOException extends RuntimeException {
    public DTOException(Exception e) {
        super(e);
    }
}
