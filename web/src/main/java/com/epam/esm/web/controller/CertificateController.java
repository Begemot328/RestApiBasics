package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.CertificateService;
import com.epam.esm.services.service.TagService;
import com.epam.esm.web.requesthandler.certificate.CertificateRequestHandler;
import com.epam.esm.web.requesthandler.tag.TagRequestHandler;
import com.epam.esm.web.exceptions.WebLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagService tagService;
    private final CertificateService certificateService;
    private final TagRequestHandler taghandler;
    private final CertificateRequestHandler certificateRequestHandler;



    @Autowired
    public CertificateController(TagService tagService,
                                 CertificateService certificateService,
                                 TagRequestHandler tagHandler, CertificateRequestHandler certificateRequestHandler) {
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.taghandler = tagHandler;
        this.certificateRequestHandler = certificateRequestHandler;
    }

    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam(value = "tag", required=false) Map<String,String> params) {
        try {
            Collection<Certificate> list;
            if (params == null || params.isEmpty()) {
                list = certificateService.find(params);
            } else {
                list = certificateService.findAll();
            }
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) {
        try {
            final Certificate certificate = certificateService.read(id);
            return certificate != null
                    ? new ResponseEntity<>(certificate, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) {
        try {
            tagService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> create(@RequestBody Certificate certificate) {
        try {
            certificate = certificateService.create(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return  new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ServiceException e) {
            return  new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> update(@RequestBody Certificate certificate) {
        try {
            certificateService.update(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return  new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ServiceException e) {
            return  new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
