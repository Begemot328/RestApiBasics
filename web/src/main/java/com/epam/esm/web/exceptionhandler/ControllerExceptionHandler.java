package com.epam.esm.web.exceptionhandler;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Object> handleServiceException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
