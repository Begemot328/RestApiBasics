package com.epam.esm.web.exceptionhandler;

import com.epam.esm.service.exceptions.ServiceLayerException;
import com.epam.esm.web.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ExceptionDTO> handleRuntimeException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ExceptionDTO> handleException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ServiceLayerException.class })
    public ResponseEntity<ExceptionDTO> handleServiceException(
            ServiceLayerException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex),
                ex.getStatus());
    }
}
