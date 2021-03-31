package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.certificate.CertificateService;
import com.epam.esm.services.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;


@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;
    private final CertificateService certificateService;

    @Autowired
    public TagController(TagService tagService,
                         CertificateService certificateService) {
        this.tagService = tagService;
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) {
        try {
            final Tag tag = tagService.read(id);
            return tag != null
                    ? new ResponseEntity<>(tag, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) {
        try {
            tagService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> create(@RequestBody Tag tag) {
        try {
            tag = tagService.create(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> update(@RequestBody Tag tag) throws ServiceException, ValidationException {
            tagService.update(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id) throws ServiceException {
            Collection<Certificate> list;
            list = certificateService.findByTag(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> add(@PathVariable(value = "id") int id,
                                           @RequestBody Certificate certificate)
            throws ServiceException, ValidationException {
            certificateService.addCertificateTag(certificate, id);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/certificates/{certificate_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "certificate_id") int certificateId)
            throws ServiceException {
            certificateService.deleteCertificateTag(certificateId, id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
