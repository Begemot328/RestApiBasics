package com.epam.esm.web.mapper;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.web.dto.CertificateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class CertificateDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public CertificateDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CertificateDTO toCertificateDTO(Certificate сertificate) {
        return Objects.isNull(сertificate) ? null : mapper.map(сertificate, CertificateDTO.class);
    }

    public Certificate toCertificate(CertificateDTO certificateDTO) {
        return mapper.map(certificateDTO, Certificate.class);
    }
}