package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.impl.CertificateServiceImpl;
import com.epam.esm.service.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;

    @Autowired
    public TagController(TagServiceImpl tagServiceImpl,
                         CertificateServiceImpl certificateServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
    }


    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String,String> params) throws ServiceException {
            Collection<Tag> list;
            if (params == null || params.isEmpty()) {
                list = tagServiceImpl.findAll();
            } else {
                list = tagServiceImpl.find(params);
            }
            return list != null &&  !list.isEmpty()
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
            list = certificateServiceImpl.findByTag(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate[]> addCertificate(@PathVariable(value = "id") int id,
                                           @RequestBody Certificate[] certificates) throws ServiceException,
            ValidationException {
        for (Certificate certificate: certificates) {
            certificateServiceImpl.addCertificateTag(certificate, id);
        }
        return new ResponseEntity<>(certificates, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/certificates/{certificate_id}")
    public ResponseEntity<Tag> deleteCertificate(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "certificate_id") int certificateId) throws ServiceException {
            certificateServiceImpl.deleteCertificateTag(certificateId, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
