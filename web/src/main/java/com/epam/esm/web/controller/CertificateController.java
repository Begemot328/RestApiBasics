package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.tag.TagService;
import com.epam.esm.web.dto.certificate.CertificateDTO;
import com.epam.esm.web.dto.certificate.CertificateDTOMapper;
import com.epam.esm.web.dto.tag.TagDTO;
import com.epam.esm.web.dto.tag.TagDTOMapper;
import com.epam.esm.web.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final TagService tagService;
    private final CertificateService certificateService;
    private final TagDTOMapper tagDTOMapper;
    private final CertificateDTOMapper certificateDTOMapper;

    @Autowired
    public CertificateController(TagService tagService,
                                 CertificateService certificateService,
                                 CertificateDTOMapper certificateDTOMapper,
                                 TagDTOMapper tagDTOMapper) {
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.certificateDTOMapper = certificateDTOMapper;
        this.tagDTOMapper = tagDTOMapper;
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<Certificate> certificates;
        certificates = certificateService.findByParameters(params);

        CollectionModel<CertificateDTO> certificateDTOs = certificateDTOMapper.toCertificateDTOList(
                certificates);
        certificateDTOs.add(linkTo(methodOn(this.getClass()).read(params)).withRel("certificates"));

        paginate(params, certificateDTOs);

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        Certificate certificate = certificateService.getById(id);
        final CertificateDTO certificateDTO = certificateDTOMapper.toCertificateDTO(certificate);
        return new ResponseEntity<>(certificateDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) throws BadRequestException {
        certificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> create(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException, BadRequestException {
        Certificate certificate = certificateService.create(
                certificateDTOMapper.toCertificate(certificateDTO));
        return new ResponseEntity<>(certificateDTOMapper.toCertificateDTO(certificate), HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException, BadRequestException, NotFoundException {
        certificateDTO = certificateDTOMapper.toCertificateDTO(
                certificateService.update(certificateDTOMapper.toCertificate(certificateDTO)));
        return new ResponseEntity<>(certificateDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<?> readTags(@PathVariable(value = "id") int id,
                                      @RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        params.put(TagSearchParameters.CERTIFICATE_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        CollectionModel<TagDTO> tagDTOs = tagDTOMapper.toTagDTOList(
                tagService.findByParameters(params));
        tagDTOs.add(linkTo(methodOn(this.getClass()).readTags(id, params)).withRel("tags"));

        paginate(params, tagDTOs);

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CertificateDTO> patchCertificate(@PathVariable(value = "id") int id,
                                                           @RequestBody CertificateDTO certificateDTO)
            throws ValidationException, BadRequestException, ServiceException, NotFoundException {
        Certificate certificate = certificateDTOMapper.toCertificate(certificateDTO);
        certificate.setId(id);
        certificate = certificateService.patch(certificate);
        return new ResponseEntity<>(certificateDTOMapper.toCertificateDTO(certificate), HttpStatus.OK);
    }

    private void paginate(MultiValueMap<String, String> params, CollectionModel collectionModel)
            throws NotFoundException, BadRequestException {
        Paginator paginator = new Paginator(params);

        paginator.setDefaultLimitIfNotLimited(params);

        collectionModel.add(linkTo(methodOn(this.getClass()).read(
                paginator.nextPage(params))).withRel("nextPage"));
        collectionModel.add(linkTo(methodOn(this.getClass()).read(
                paginator.previousPage(params))).withRel("previousPage"));
    }
}
