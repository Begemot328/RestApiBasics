package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Certificate;

public class CertificateDTOConverter {

    public static Certificate convertDTO(CertificateDTO dto) {
        Certificate certificate = new Certificate(dto.getName(), dto.getPrice(),
        dto.getDuration());
        certificate.setDescription(dto.getDescription());
        certificate.setId(dto.getId());
        return certificate;
    }

    public static CertificateDTO convertObject(Certificate certificate) {
        if(certificate == null) {
            return null;
        }
        CertificateDTO certificateDTO = new CertificateDTO(certificate.getName(),
                certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getCreateDate(), certificate.getLastUpdateDate());
        certificateDTO.setId(certificate.getId());
        return certificateDTO;
    }
}
