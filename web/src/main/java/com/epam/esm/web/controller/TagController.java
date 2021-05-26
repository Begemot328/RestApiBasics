package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.service.tag.TagServiceImpl;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        CollectionModel<TagDTO> tagDTOs = tagDTOMapper.toTagDTOList(tags);
        tagDTOs.add(linkTo(methodOn(this.getClass()).read(params)).withSelfRel());

        paginate(params, tagDTOs);

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
        TagDTO tagDTO = tagDTOMapper.toTagDTO(tag);
        tagDTO.add(linkTo(methodOn(this.getClass()).readMostPopular()).withRel("most-popular"));
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
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO)
            throws ValidationException, BadRequestException {
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
        certificateDTOs.add(
                linkTo(methodOn(this.getClass()).readCertificates(id, params)).withRel("certificates"));

        paginate(params, certificateDTOs);

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    private void paginate(MultiValueMap<String, String> params, CollectionModel collectionModel)
            throws NotFoundException, BadRequestException {
        Paginator paginator = new Paginator(params);

        if(paginator.isLimited(params)) {
            collectionModel.add(linkTo(methodOn(this.getClass()).read(
                    paginator.nextPage(params))).withRel("nextPage"));
            collectionModel.add(linkTo(methodOn(this.getClass()).read(
                    paginator.previousPage(params))).withRel("previousPage"));
        }
    }
}
