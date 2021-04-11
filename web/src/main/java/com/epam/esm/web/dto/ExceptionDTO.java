package com.epam.esm.web.dto;

import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.web.exceptions.ControllerException;

public class ExceptionDTO {
    private String errorMessage;
    private int errorCode;

    private ExceptionDTO() {}

    public ExceptionDTO(Exception e, int errorCode) {
        this.errorMessage = e.getMessage();
        this.errorCode = errorCode;
    }

    public ExceptionDTO(ControllerException e) {
        this.errorMessage = e.getMessage();
        this.errorCode = e.getErrorCode();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
