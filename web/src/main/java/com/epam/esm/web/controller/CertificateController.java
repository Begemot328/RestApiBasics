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
import org.springframework.util.CollectionUtils;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;

    @Autowired
    public CertificateController(TagServiceImpl tagServiceImpl,
                                 CertificateServiceImpl certificateServiceImpl){
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
    }

    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String,String> params) throws ServiceException {
            Collection<Certificate> list;
            if (CollectionUtils.isEmpty(params)) {
                list = certificateServiceImpl.readAll();
            } else {
                list = certificateServiceImpl.read(params);
            }
            return !CollectionUtils.isEmpty(list)
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws ServiceException {
            final Certificate certificate = certificateServiceImpl.read(id);
            return certificate != null
                    ? new ResponseEntity<>(certificate, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) throws ServiceException {
            certificateServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> create(@RequestBody Certificate certificate)
            throws ServiceException, ValidationException {
            certificate = certificateServiceImpl.create(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> update(@RequestBody Certificate certificate)
            throws ServiceException, ValidationException {
            certificateServiceImpl.update(certificate);
            return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id) throws ServiceException {
            List<Tag> list;
            list = tagServiceImpl.readByCertificate(id);
            return list != null &&  !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag[]> createTag(@PathVariable(value = "id") int id,
                                         @RequestBody Tag[] tags)
            throws ServiceException, ValidationException {
        for (Tag tag: tags) {
            certificateServiceImpl.addCertificateTag(id, tag);
        }
        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/tags/{tag_id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(value = "id") int id,
                                      @PathVariable(value = "tag_id") int tagId)
            throws ServiceException {
            certificateServiceImpl.deleteCertificateTag(id, tagId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
