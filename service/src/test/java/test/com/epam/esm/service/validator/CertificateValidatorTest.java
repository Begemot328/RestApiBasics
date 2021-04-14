package test.com.epam.esm.service.validator;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CertificateValidatorTest {
    CertificateValidator validator = new CertificateValidator();

    @Test
    void testValidateIfEmptyName() {
        Certificate certificate = new Certificate("",
                null, BigDecimal.valueOf(10.0), 3, null, null);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void testValidateIfNullName() {
        Certificate certificate = new Certificate(null,
                null, BigDecimal.valueOf(10.0), 3, null, null);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void testValidateIfZeroDuration() {
        Certificate certificate = new Certificate("Certificate1",
                null, BigDecimal.valueOf(10.0), 0, null, null);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void testValidateIfZeroPrice() {
        Certificate certificate = new Certificate("Certificate1",
                null, BigDecimal.valueOf(0), 3, null, null);
        assertThrows(ValidationException.class, () -> validator.validate(certificate));
    }

    @Test
    void testValidate() {
        Certificate certificate = new Certificate("Certificate1",
                null, BigDecimal.valueOf(10.0), 3, null, null);
        assertDoesNotThrow(() -> validator.validate(certificate));
    }
}
