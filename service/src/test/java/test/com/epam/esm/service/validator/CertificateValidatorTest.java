package test.com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CertificateValidatorTest {
    CertificateValidator validator = new CertificateValidator();

    @Test
    void validate_EmptyName_throwsException() {
        Certificate certificate = new Certificate("",
                BigDecimal.valueOf(10.0), 3);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void validate_NullName_throwsException() {
        Certificate certificate = new Certificate(null,
                BigDecimal.valueOf(10.0), 3);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void validate_zeroDuration_throwsException() {
        Certificate certificate = new Certificate("Certificate1",
                BigDecimal.valueOf(10.0), 0);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void validate_zeroPrice_throwsException() {
        Certificate certificate = new Certificate("Certificate1",
                BigDecimal.valueOf(0), 3);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void validate_valid_doNothing() {
        Certificate certificate = new Certificate("Certificate1",
                BigDecimal.valueOf(10.0), 3);
        assertDoesNotThrow(() -> validator.validate(certificate));
    }
}
