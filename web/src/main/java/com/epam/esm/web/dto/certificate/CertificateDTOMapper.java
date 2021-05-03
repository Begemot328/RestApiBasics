package com.epam.esm.web.dto.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.exceptions.DTOException;
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
    private ModelMapper mapper;

    @Autowired
    public CertificateDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CertificateDTO toCertificateDTO(Certificate certificate) {
        CertificateDTO certificateDTO = mapper.map(certificate, CertificateDTO.class);
        try {
            Link link = linkTo(methodOn(CertificateController.class).read(certificate.getId())).withSelfRel();
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
