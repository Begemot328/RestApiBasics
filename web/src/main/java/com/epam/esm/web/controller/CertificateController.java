package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.impl.CertificateServiceImpl;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.web.dto.CertificateDTO;
import com.epam.esm.web.dto.CertificateDTOConverter;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;
    private static final int ERROR_CODE_SUFFIX = 01;

    @Autowired
    public CertificateController(TagServiceImpl tagServiceImpl,
                                 CertificateServiceImpl certificateServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
    }

    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam Map<String, String> params)
            throws ServiceException, BadRequestException {
        Collection<CertificateDTO> list;
            if (CollectionUtils.isEmpty(params)) {

                list = certificateServiceImpl.readAll()
                        .stream().map(CertificateDTOConverter::convertObject).collect(Collectors.toList());

            } else {
                list = certificateServiceImpl.read(params)
                        .stream().map(CertificateDTOConverter::convertObject).collect(Collectors.toList());
            }
        return !CollectionUtils.isEmpty(list)
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws ServiceException {
        Certificate certificate = certificateServiceImpl.read(id);
        final CertificateDTO certificateDTO = CertificateDTOConverter.convertObject(certificate);
        return certificate != null
                ? new ResponseEntity<>(certificateDTO, HttpStatus.OK)
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
    public ResponseEntity<CertificateDTO> create(@RequestBody CertificateDTO certificateDTO)
            throws ServiceException, ValidationException {
        Certificate certificate = certificateServiceImpl.create(
                CertificateDTOConverter.convertDTO(certificateDTO));
        return new ResponseEntity<>(CertificateDTOConverter.convertObject(certificate), HttpStatus.CREATED);
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
        return list != null && !list.isEmpty()
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag[]> createTag(@PathVariable(value = "id") int id,
                                           @RequestBody Tag[] tags)
            throws ServiceException, ValidationException, BadRequestException {
        for (Tag tag : tags) {
            certificateServiceImpl.addCertificateTag(id, tag);
        }
        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/tags/{tag_id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(value = "id") int id,
                                         @PathVariable(value = "tag_id") int tagId)
            throws ServiceException, BadRequestException {
        certificateServiceImpl.deleteCertificateTag(id, tagId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
