package test.com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {
    UserValidator validator = new UserValidator();
    private User user;

    @BeforeEach
    void init() {
        user = new User("Yury", "Zmushko", "root", "qwerty");
    }

    @Test
    void validate_NullFirstName_throwsException() {
        user.setFirstName(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_EmptyFirstName_throwsException() {
        user.setFirstName("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_NullLastName_throwsException() {
        user.setLastName(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_EmptyLastName_throwsException() {
        user.setLastName("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_NullLogin_throwsException() {
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_EmptyLogin_throwsException() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_NullPassword_throwsException() {
        user.setPassword(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_EmptyPassword_throwsException() {
        user.setPassword("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

    @Test
    void validate_valid_doNothing() {
        assertDoesNotThrow(() -> validator.validate(user));
    }
}
