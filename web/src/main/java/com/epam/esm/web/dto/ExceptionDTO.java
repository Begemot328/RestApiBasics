package com.epam.esm.web.dto;

import com.epam.esm.service.exceptions.ServiceLayerException;

public class ExceptionDTO {
    private String errorMessage;
    private int errorCode;

    private ExceptionDTO() {
    }

    public ExceptionDTO(Exception e, int errorCode) {
        this.errorMessage = e.getMessage();
        this.errorCode = errorCode;
    }

    public ExceptionDTO(ServiceLayerException e) {
        this.errorMessage = e.getMessage();
        this.errorCode = e.getErrorCode();
    }
}
