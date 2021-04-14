package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ServiceLayerException;
import com.epam.esm.service.service.impl.CertificateServiceImpl;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.web.dto.CertificateDTO;
import com.epam.esm.web.dto.CertificateDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> readAll(@RequestParam Map<String, String> params)
            throws BadRequestException, NotFoundException {
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
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        final Tag tag = tagServiceImpl.read(id);
        return tag != null
                ? new ResponseEntity<>(tag, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) throws BadRequestException {
        tagServiceImpl.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> create(@RequestBody Tag tag) throws ServiceException {
        tag = tagServiceImpl.create(tag);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> readCertificates(@PathVariable(value = "id") int id) throws NotFoundException {
        Collection<CertificateDTO> list = certificateServiceImpl.readByTag(id)
                .stream().map(CertificateDTOConverter::convertObject)
                .collect(Collectors.toList());
        return !CollectionUtils.isEmpty(list)
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificate(@PathVariable(value = "id") int id,
                                            @RequestBody Certificate[] certificates) throws ServiceLayerException {
        certificateServiceImpl.addCertificatesTag(certificates, id);
        List<CertificateDTO> list = Arrays.stream(certificates)
                .map(CertificateDTOConverter::convertObject).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/certificates/{certificate_id}")
    public ResponseEntity<Tag> deleteCertificate(@PathVariable(value = "id") int id,
                                                 @PathVariable(value = "certificate_id") int certificateId)
            throws BadRequestException {
        certificateServiceImpl.deleteCertificateTag(certificateId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
