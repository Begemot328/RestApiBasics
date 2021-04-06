package test.com.epam.esm.service.validator;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagValidatorTest {
    TagValidator validator = new TagValidator();

    @Test
    void validateTest() throws ValidationException {
        Tag tag = new Tag(null);
        assertThrows(ValidationException.class, () -> validator.validate(tag));
    }

    @Test
    void validateTest2() throws ValidationException {
        Tag tag = new Tag("");
        assertThrows(ValidationException.class, () -> validator.validate(tag));
    }

    @Test
    void validateTest3() throws ValidationException {
        Tag tag = new Tag("Tagname");
        assertDoesNotThrow(() -> validator.validate(tag));
    }
}
