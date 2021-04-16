package com.epam.esm.service.validator;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * {@link Certificate} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class CertificateValidator implements EntityValidator<Certificate> {

    @Override
    public void validate(Certificate certificate) throws ValidationException {
        if (certificate.getName() == null) {
            throw new ValidationException("Certificate name can't be null!",
                    ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
        }
        if (certificate.getName().isEmpty()) {
            throw new ValidationException("Certificate name can't be empty!",
                    ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
        }
        if (certificate.getDuration() <= 0) {
            throw new ValidationException("Certificate duration must be positive!",
                    ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
        }
        if (certificate.getPrice() == null) {
            throw new ValidationException("Certificate price can't be null!",
                    ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
        }
        if (certificate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Certificate price must be positive!",
                    ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
        }
    }
}
