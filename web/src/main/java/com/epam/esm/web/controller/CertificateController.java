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
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagService tagService;
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(TagService tagService,
                                 CertificateService certificateService){
        this.tagService = tagService;
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String,String> params) throws ServiceException {
            Collection<Certificate> list;
            if (params == null || params.isEmpty()) {
                list = certificateService.findAll();
            } else {
                list = certificateService.find(params);
            }
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws ServiceException {
            final Certificate certificate = certificateService.read(id);
            return certificate != null
                    ? new ResponseEntity<>(certificate, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) throws ServiceException {
            certificateService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> create(@RequestBody Certificate certificate)
            throws ServiceException, ValidationException {
            certificate = certificateService.create(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> update(@RequestBody Certificate certificate)
            throws ServiceException, ValidationException {
            certificateService.update(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id) throws ServiceException {
            Collection<Tag> list;
            list = tagService.findByCertificate(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> createTag(@PathVariable(value = "id") int id,
                                      @RequestBody Tag tag)
            throws ServiceException, ValidationException {
            certificateService.addCertificateTag(id, tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/tags/{tag_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> deleteTag(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "tag_id") int tagId)
            throws ServiceException {
            certificateService.deleteCertificateTag(id, tagId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
