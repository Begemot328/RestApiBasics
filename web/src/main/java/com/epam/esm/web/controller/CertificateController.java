package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;

    @Autowired
    public CertificateController(TagServiceImpl tagServiceImpl,
                                 CertificateServiceImpl certificateServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
    }

    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String, String> params)
            throws NotFoundException, BadRequestException {
        Collection<CertificateDTO> list;
            if (CollectionUtils.isEmpty(params)) {
                list = certificateServiceImpl.readAll()
                        .stream().map(CertificateDTOConverter::convertObject).collect(Collectors.toList());
            } else {
                list = certificateServiceImpl.read(params)
                        .stream().map(CertificateDTOConverter::convertObject).collect(Collectors.toList());
            }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        Certificate certificate = certificateServiceImpl.read(id);
        final CertificateDTO certificateDTO = CertificateDTOConverter.convertObject(certificate);
        return new ResponseEntity<>(certificateDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) throws BadRequestException {
        certificateServiceImpl.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> create(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException, ServiceException {
        Certificate certificate = certificateServiceImpl.create(
                CertificateDTOConverter.convertDTO(certificateDTO));
        return new ResponseEntity<>(CertificateDTOConverter.convertObject(certificate), HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException {
        certificateDTO = CertificateDTOConverter.convertObject(
                certificateServiceImpl.update(CertificateDTOConverter.convertDTO(certificateDTO)));
        return new ResponseEntity<>(certificateDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id) throws NotFoundException {
        List<Tag> list = tagServiceImpl.readByCertificate(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag[]> createTag(@PathVariable(value = "id") int id,
                                           @RequestBody Tag[] tags)
            throws ServiceException, BadRequestException {
        certificateServiceImpl.addCertificateTags(id, tags);
        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/tags/{tag_id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(value = "id") int id,
                                         @PathVariable(value = "tag_id") int tagId)
            throws BadRequestException {
        certificateServiceImpl.deleteCertificateTag(id, tagId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
