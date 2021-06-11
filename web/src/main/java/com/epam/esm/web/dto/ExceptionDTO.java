package com.epam.esm.web.dto;

import com.epam.esm.service.exceptions.ServiceLayerException;

/**
 * {@link Exception} DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ExceptionDTO {
    private String errorMessage;
    private int errorCode;

    /**
     * Default constructor.
     */
    public ExceptionDTO() {
        // Default constructor for Model mapper purposes.
    }

    /**
     * Constructor.
     *
     * @param e         {@link Exception} to wrap.
     * @param errorCode Error code.
     */
    public ExceptionDTO(Exception e, int errorCode) {
        this.errorMessage = e.getMessage();
        this.errorCode = errorCode;
    }

    /**
     * Constructor.
     *
     * @param errorMessage Error message.
     * @param errorCode    Error code.
     */
    public ExceptionDTO(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * Constructor.
     *
     * @param e {@link ServiceLayerException} to wrap.
     */
    public ExceptionDTO(ServiceLayerException e) {
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
