package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ServiceLayerException;
import com.epam.esm.service.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.service.tag.TagServiceImpl;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<Tag> tags = new ArrayList<>();
        if (CollectionUtils.isEmpty(params)) {
            tags = tagServiceImpl.readAll();
        } else {
            tags = tagServiceImpl.read(params);
        }
        Paginator paginator = new Paginator(params);
        CollectionModel<TagDTO> tagDTOs = tagDTOMapper.toTagDTOList(tags);
        tagDTOs.add(linkTo(methodOn(this.getClass()).read(params)).withSelfRel());

        if (paginator.isLimited(params)) {
            tagDTOs.add(linkTo(methodOn(this.getClass()).read(
                    paginator.nextPage(params))).withRel("next page"));
            tagDTOs.add(linkTo(methodOn(this.getClass()).read(
                    paginator.previousPage(params))).withRel("previous page"));
        }
        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id") int id) throws NotFoundException {
        final Tag tag = tagServiceImpl.read(id);
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<?> readMostPopular() throws NotFoundException {
        final Tag tag = tagServiceImpl.readMostlyUsedTag();
        Link link = linkTo(methodOn(TagController.class).readMostPopular()).withRel("most-popular");
        TagDTO tagDTO = tagDTOMapper.toTagDTO(tag);
        tagDTO.add(link);
        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) throws BadRequestException {
        tagServiceImpl.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO) throws ServiceException {
        Tag tag = tagServiceImpl.create(tagDTOMapper.toTag(tagDTO));
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> readCertificates(@PathVariable(value = "id") int id,
                                              @RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        params.put(CertificateSearchParameters.TAG_ID.getParameterName(), Collections.singletonList(Integer.toString(id)));
        CollectionModel<CertificateDTO> certificateDTOs = certificateDTOMapper.toCertificateDTOList(
                certificateServiceImpl.read(params));
        Link link = linkTo(methodOn(TagController.class).readCertificates(id, params)).withRel("certificates");
        certificateDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if (paginator.isLimited(params)) {
            certificateDTOs.add(linkTo(methodOn(this.getClass()).readCertificates(
                    id, paginator.nextPage(params))).withRel("nextPage"));
            certificateDTOs.add(linkTo(methodOn(this.getClass()).readCertificates(
                    id, paginator.previousPage(params))).withRel("previousPage"));
        }
        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
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
