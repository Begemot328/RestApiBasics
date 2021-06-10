package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * {@link Tag} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class TagValidator implements EntityValidator<Tag> {

    @Override
    public void validate(Tag tag) throws ValidationException {
       validateName(tag);
    }

    private void validateName(Tag tag) throws ValidationException {
        validateNotEmptyString(tag.getName(), "Tag name",
                ErrorCodes.TAG_VALIDATION_EXCEPTION);
    }
}
