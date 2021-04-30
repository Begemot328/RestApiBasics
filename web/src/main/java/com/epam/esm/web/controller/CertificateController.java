package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
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
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
        if (CollectionUtils.isEmpty(params)) {
            certificates = certificateService.readAll();
        } else {
            certificates = certificateService.read(params);
        }
        CollectionModel<CertificateDTO> certificateDTOs = certificateDTOMapper.toCertificateDTOList(
                certificates);
        Link link = linkTo(methodOn(this.getClass()).read(params)).withRel("certificates");
        certificateDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if(paginator.isLimited(params)) {
            certificateDTOs.add(linkTo(methodOn(this.getClass()).read(
                    paginator.nextPage(params))).withRel("nextPage"));
            certificateDTOs.add(linkTo(methodOn(this.getClass()).read(
                    paginator.previousPage(params))).withRel("previousPage"));
        }

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        Certificate certificate = certificateService.read(id);
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
            throws ValidationException, ServiceException {
        Certificate certificate = certificateService.create(
                certificateDTOMapper.toCertificate(certificateDTO));
        return new ResponseEntity<>(certificateDTOMapper.toCertificateDTO(certificate), HttpStatus.CREATED);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO)
            throws ValidationException {
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
                tagService.read(params));
        Link link = linkTo(methodOn(this.getClass()).readTags(id, params)).withRel("tags");
        tagDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if(paginator.isLimited(params)) {
            tagDTOs.add(linkTo(methodOn(this.getClass()).readTags(
                    id, paginator.nextPage(params))).withRel("nextPage"));
            tagDTOs.add(linkTo(methodOn(this.getClass()).readTags(
                    id, paginator.previousPage(params))).withRel("previousPage"));
        }
        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag[]> createTag(@PathVariable(value = "id") int id,
                                           @RequestBody Tag[] tags)
            throws ServiceException, BadRequestException {
        certificateService.addCertificateTags(id, tags);
        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/tags/{tag_id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(value = "id") int id,
                                         @PathVariable(value = "tag_id") int tagId)
            throws BadRequestException {
        certificateService.deleteCertificateTag(id, tagId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CertificateDTO> patchCertificate(@PathVariable(value = "id") int id,
                                                           @RequestBody CertificateDTO certificateDTO)
            throws ValidationException, BadRequestException {
        Certificate certificate = certificateDTOMapper.toCertificate(certificateDTO);
        certificate.setId(id);
        certificate = certificateService.patch(certificate);
        return new ResponseEntity<>(certificateDTOMapper.toCertificateDTO(certificate), HttpStatus.OK);
    }

}
