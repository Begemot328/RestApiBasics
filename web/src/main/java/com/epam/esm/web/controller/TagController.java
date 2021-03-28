package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.CertificateService;
import com.epam.esm.services.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<?> readAll(
            @RequestParam(value = "certificate", required=false) Integer id) {
        try {
            Collection<Tag> list;
            if (id != null) {
                list = tagService.findAll(certificateService.read(id));
            } else {
                list = tagService.findAll();
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
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        try {
            tagService.update(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
