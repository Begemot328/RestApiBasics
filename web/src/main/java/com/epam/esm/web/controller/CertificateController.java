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
import com.epam.esm.web.dto.CertificateDTOMapper;
import com.epam.esm.web.dto.TagDTO;
import com.epam.esm.web.dto.TagDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;
    private final TagDTOMapper tagDTOMapper;
    private final CertificateDTOMapper certificateDTOMapper;

    @Autowired
    public CertificateController(TagServiceImpl tagServiceImpl,
                                 CertificateServiceImpl certificateServiceImpl,
                                 CertificateDTOMapper certificateDTOMapper,
                                 TagDTOMapper tagDTOMapper) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
        this.certificateDTOMapper = certificateDTOMapper;
        this.tagDTOMapper = tagDTOMapper;
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<CertificateDTO> certificates;
        if (CollectionUtils.isEmpty(params)) {
            certificates = certificateServiceImpl.readAll()
                    .stream().map(certificateDTOMapper::convertObject).collect(Collectors.toList());
        } else {
            certificates = certificateServiceImpl.read(params)
                    .stream().map(certificateDTOMapper::convertObject).collect(Collectors.toList());
        }
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        Certificate certificate = certificateServiceImpl.read(id);
        final CertificateDTO certificateDTO = certificateDTOMapper.convertObject(certificate);
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
                certificateDTOMapper.convertDTO(certificateDTO));
        return new ResponseEntity<>(certificateDTOMapper.convertObject(certificate), HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException {
        certificateDTO = certificateDTOMapper.convertObject(
                certificateServiceImpl.update(certificateDTOMapper.convertDTO(certificateDTO)));
        return new ResponseEntity<>(certificateDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id) throws NotFoundException {
        List<TagDTO> list = tagServiceImpl.readByCertificate(id)
                .stream().map(tagDTOMapper::toTagDTO).collect(Collectors.toList());
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
