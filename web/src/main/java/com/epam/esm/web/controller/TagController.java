package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.tag.TagService;
import com.epam.esm.web.dto.certificate.CertificateDTO;
import com.epam.esm.web.dto.certificate.CertificateDTOMapper;
import com.epam.esm.web.dto.tag.TagDTO;
import com.epam.esm.web.dto.tag.TagDTOMapper;
import com.epam.esm.model.entity.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tags")
public class TagController implements PageableSearch {

    private final TagService tagService;
    private final CertificateService certificateService;
    private final TagDTOMapper tagDTOMapper;
    private final CertificateDTOMapper certificateDTOMapper;

    @Autowired
    public TagController(TagService tagService,
                         CertificateService certificateService,
                         CertificateDTOMapper certificateDTOMapper,
                         TagDTOMapper tagDTOMapper) {
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.certificateDTOMapper = certificateDTOMapper;
        this.tagDTOMapper = tagDTOMapper;
    }

    @GetMapping
    public ResponseEntity<?> find(@RequestParam MultiValueMap<String, String> params, Pageable pageable)
            throws BadRequestException, NotFoundException {
        List<Tag> tags = tagService.findByParameters(params, pageable);
        CollectionModel<TagDTO> tagDTOs = tagDTOMapper.toTagDTOList(tags);
        tagDTOs.add(linkTo(methodOn(this.getClass()).find(params, pageable)).withSelfRel());

        paginate(params, tagDTOs, pageable);

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") int id) throws NotFoundException {
        final Tag tag = tagService.getById(id);
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<?> getMostPopular() throws NotFoundException {
        final Tag tag = tagService.findMostPopularTag();
        TagDTO tagDTO = tagDTOMapper.toTagDTO(tag);
        tagDTO.add(linkTo(methodOn(this.getClass()).getMostPopular()).withRel("most-popular"));
        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @Secured(Roles.ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Tag> delete(@PathVariable(value = "id") int id) throws BadRequestException {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured(Roles.ADMIN)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO)
            throws ValidationException, BadRequestException {
        Tag tag = tagService.create(tagDTOMapper.toTag(tagDTO));
        return new ResponseEntity<>(tagDTOMapper.toTagDTO(tag), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<?> findCertificates(@PathVariable(value = "id") int id,
                                              @RequestParam MultiValueMap<String, String> params,
                                              Pageable pageable)
            throws NotFoundException, BadRequestException {
        params.put(CertificateSearchParameters.TAG_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        CollectionModel<CertificateDTO> certificateDTOs = certificateDTOMapper.toCertificateDTOList(
                certificateService.findByParameters(params, pageable));
        certificateDTOs.add(
                linkTo(methodOn(this.getClass()).findCertificates(id, params, pageable)).withRel("certificates"));

        paginate(params, certificateDTOs, pageable);

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }
}
