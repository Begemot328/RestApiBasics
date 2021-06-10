package test.com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidatorTest {
    OrderValidator validator = new OrderValidator();
    private Order order;

    @BeforeEach
    void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Certificate certificate = new Certificate("nastolka.by",
                BigDecimal.valueOf(40.1), 50);
        certificate.setDescription("board games certificate");
        certificate.setCreateDate(LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        certificate.setId(2);

        User user = new User("Ivan", "Ivanov", "Ivanov", "qwerty");
        user.setId(2);

        order = new Order(certificate, user, BigDecimal.valueOf(10.0), 1,
                LocalDateTime.parse("2021-01-22 09:20:11", formatter));
    }

    @Test
    void validate_NullCertificate_throwsException() {
        order.setCertificate(null);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_NullUser_throwsException() {
        order.setUser(null);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_NullPurchaseDate_throwsException() {
        order.setPurchaseDate(null);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_ZeroAmount_throwsException() {
        order.setOrderAmount(BigDecimal.ZERO);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_ZeroQuantity_throwsException() {
        order.setCertificateQuantity(0);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_InvalidUser_throwsException() {
        order.getUser().setFirstName(null);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    @Test
    void validate_InvalidCertificate_throwsException() {
        order.getCertificate().setName(null);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

}
