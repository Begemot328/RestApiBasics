package com.epam.esm.services.validator;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.services.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CertificateValidator implements EntityValidator<Certificate>{
    @Override
    public void validate(Certificate certificate) throws ValidationException {
        if (certificate.getName() == null) {
            throw new ValidationException("Null name!");
        } else {
            if (certificate.getName().isEmpty()) {
                throw new ValidationException("Empty name!");
            }
        }
        if (certificate.getDuration() <= 0) {
            throw new ValidationException("Non-positive duration!");
        }
        if (certificate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Non-positive price!");
        }

    }
}
