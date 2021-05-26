package com.epam.esm.service.validator;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

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
        validateName(certificate);
        validateDuration(certificate);
        validatePrice(certificate);
    }

    private void validateName(Certificate certificate) throws ValidationException {
        validateNotEmptyString(certificate.getName(), "Certificate name",
                ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
    }

    private void validateDuration(Certificate certificate) throws ValidationException {
        validatePositiveNumber(certificate.getDuration(), "Certificate duration",
                ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
    }

    private void validatePrice(Certificate certificate) throws ValidationException {
        validatePositiveNumber(certificate.getPrice(), "Certificate duration",
                ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION);
    }
}
