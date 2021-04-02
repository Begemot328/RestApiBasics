package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
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
import java.util.Map;


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


    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String,String> params) throws ServiceException {
            Collection<Tag> list;
            if (params == null || params.isEmpty()) {
                list = tagService.findAll();
            } else {
                list = tagService.find(params);
            }
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws ServiceException {
            final Tag tag = tagService.read(id);
            return tag != null
                    ? new ResponseEntity<>(tag, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) throws ServiceException {
            tagService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> create(@RequestBody Tag tag) throws ServiceException, ValidationException {
            tag = tagService.create(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> update(@RequestBody Tag tag) throws ServiceException, ValidationException {
            tagService.update(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> readCertificates(@PathVariable(value = "id") int id) throws ServiceException {
            Collection<Certificate> list;
            list = certificateService.findByTag(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> addCertificate(@PathVariable(value = "id") int id,
                                           @RequestBody Certificate certificate) throws ServiceException,
            ValidationException {
            certificateService.addCertificateTag(certificate, id);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/certificates/{certificate_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> deleteCertificate(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "certificate_id") int certificateId) throws ServiceException {
            certificateService.deleteCertificateTag(certificateId, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
