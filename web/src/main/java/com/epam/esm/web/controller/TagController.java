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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagServiceImpl tagServiceImpl;
    private final CertificateServiceImpl certificateServiceImpl;
    private final TagDTOMapper tagDTOMapper;
    private final CertificateDTOMapper certificateDTOMapper;

    @Autowired
    public TagController(TagServiceImpl tagServiceImpl,
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
            throws BadRequestException, NotFoundException {
        List<TagDTO> list;
        if (CollectionUtils.isEmpty(params)) {
            list = tagServiceImpl.readAll()
                    .stream().map(tagDTOMapper::toTagDTO).collect(Collectors.toList());
        } else {
            list = tagServiceImpl.read(params)
                    .stream().map(tagDTOMapper::toTagDTO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        final Tag tag = tagServiceImpl.read(id);
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<?> readMostPopular() throws NotFoundException {
        final Tag tag = tagServiceImpl.readMostlyUsedTag();
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.OK);
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
        List<CertificateDTO> list = certificateServiceImpl.readByTag(id)
                .stream().map(certificateDTOMapper::toCertificateDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/certificates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificate(@PathVariable(value = "id") int id,
                                            @RequestBody Certificate[] certificates) throws ServiceLayerException {
        certificateServiceImpl.addCertificatesTag(certificates, id);
        List<CertificateDTO> list = Arrays.stream(certificates)
                .map(certificateDTOMapper::toCertificateDTO).collect(Collectors.toList());
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
