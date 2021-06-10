package com.epam.esm.web.dto.certificate;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.dto.tag.TagDTO;
import com.epam.esm.web.dto.tag.TagDTOMapper;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateDTOMapper {
    private final ModelMapper mapper;

    @Autowired
    public CertificateDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;

        Converter<List<Tag>, List<TagDTO>> converterToDTO =
                src -> src.getSource().stream().map(tag -> new TagDTOMapper(this.mapper)
                        .toTagDTO(tag)).collect(Collectors.toList());

        Converter<List<TagDTO>, List<Tag>> converterToObject =
                src -> src.getSource().stream().map(tagDTO -> new TagDTOMapper(this.mapper)
                        .toTag(tagDTO)).collect(Collectors.toList());

        mapper.typeMap(Certificate.class, CertificateDTO.class).addMappings(
                newMapper -> newMapper.using(converterToDTO).map(
                        Certificate::getTags, CertificateDTO::setTags));

        mapper.typeMap(CertificateDTO.class, Certificate.class).addMappings(
                newMapper -> newMapper.using(converterToObject).map(
                        CertificateDTO::getTags, Certificate::setTags));
    }

    public CertificateDTO toCertificateDTO(Certificate certificate) {
        CertificateDTO certificateDTO = mapper.map(certificate, CertificateDTO.class);
        try {
            Link link = linkTo(methodOn(CertificateController.class).get(certificate.getId()))
                    .withSelfRel();
            certificateDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return certificateDTO;
    }

    public CollectionModel<CertificateDTO> toCertificateDTOList(List<Certificate> certificates) {
        return CollectionModel.of(
                certificates.stream().map(this::toCertificateDTO).collect(Collectors.toList()));
    }

    public Certificate toCertificate(CertificateDTO certificateDTO) {
        return mapper.map(certificateDTO, Certificate.class);
    }
}
