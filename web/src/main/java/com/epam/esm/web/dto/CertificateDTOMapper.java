package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CertificateDTOMapper {

    public Certificate convertDTO(CertificateDTO dto) {
        Certificate certificate = new Certificate(dto.getName(), BigDecimal.valueOf(dto.getPrice()),
        dto.getDuration());
        certificate.setDescription(dto.getDescription());
        certificate.setId(dto.getId());
        return certificate;
    }

    public CertificateDTO convertObject(Certificate certificate) {
        if(certificate == null) {
            return null;
        }
        CertificateDTO certificateDTO = new CertificateDTO(certificate.getName(),
                certificate.getDescription(), certificate.getPrice().doubleValue(),
                certificate.getDuration(), certificate.getCreateDate(), certificate.getLastUpdateDate());
        certificateDTO.setId(certificate.getId());
        return certificateDTO;
    }
}
