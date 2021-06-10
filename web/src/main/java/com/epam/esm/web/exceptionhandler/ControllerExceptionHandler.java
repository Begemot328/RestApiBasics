package com.epam.esm.web.exceptionhandler;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ServiceLayerException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.web.dto.ExceptionDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<ExceptionDTO> handleLockedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex, HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ExceptionDTO> handleDataIntegrityViolationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ExceptionDTO> handleServiceException(
            ServiceLayerException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ExceptionDTO> handleBadRequestException(
            ServiceLayerException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ExceptionDTO> handleValidationException(
            ServiceLayerException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ExceptionDTO> handleNotFoundException(
            ServiceLayerException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ExceptionDTO(ex),
                HttpStatus.NOT_FOUND);
    }
}
