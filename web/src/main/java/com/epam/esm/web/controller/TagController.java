package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.impl.CertificateServiceImpl;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.web.dto.ExceptionDTO;
import com.epam.esm.web.exceptions.ControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private static final int ERROR_CODE_SUFFIX = 02;
    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;

    @Autowired
    public TagController(TagServiceImpl tagServiceImpl,
                         CertificateServiceImpl certificateServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
    }


    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String,String> params)
            throws ServiceException, BadRequestException {
            Collection<Tag> list;
            if (CollectionUtils.isEmpty(params)) {
                list = tagServiceImpl.readAll();
            } else {
                list = tagServiceImpl.read(params);
            }
            return !CollectionUtils.isEmpty(list)
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws ServiceException {
            final Tag tag = tagServiceImpl.read(id);
            return tag != null
                    ? new ResponseEntity<>(tag, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) throws ServiceException {
            tagServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> create(@RequestBody Tag tag) throws ServiceException, ValidationException {
        tag = tagServiceImpl.create(tag);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> readCertificates(@PathVariable(value = "id") int id) throws ServiceException {
            Collection<Certificate> list;
            list = certificateServiceImpl.readByTag(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate[]> addCertificate(@PathVariable(value = "id") int id,
                                           @RequestBody Certificate[] certificates) throws ServiceException,
            ValidationException, BadRequestException {
        for (Certificate certificate: certificates) {
            certificateServiceImpl.addCertificateTag(certificate, id);
        }
        return new ResponseEntity<>(certificates, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/certificates/{certificate_id}")
    public ResponseEntity<Tag> deleteCertificate(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "certificate_id") int certificateId) throws ServiceException, ControllerException, BadRequestException {
            certificateServiceImpl.deleteCertificateTag(certificateId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<ExceptionDTO> handleBadRequestException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<ExceptionDTO>(
                new ExceptionDTO(ex, HttpStatus.BAD_REQUEST.value() * 100 + ERROR_CODE_SUFFIX),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<ExceptionDTO> handleValidationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<ExceptionDTO>(
                new ExceptionDTO(ex, HttpStatus.UNPROCESSABLE_ENTITY.value() * 100 + ERROR_CODE_SUFFIX),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<ExceptionDTO> handleServiceException(
            Exception ex, WebRequest request) throws ControllerException {
        throw new ControllerException(ex, HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + ERROR_CODE_SUFFIX);
    }
}
