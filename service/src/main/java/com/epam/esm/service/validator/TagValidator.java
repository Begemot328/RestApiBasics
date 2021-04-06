package com.epam.esm.service.validator;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * {@link Tag} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class TagValidator implements EntityValidator<Tag>{

    @Override
    public void validate(Tag tag) throws ValidationException {
        if (tag.getName() == null) {
            throw new ValidationException("Null name!");
        } else {
            if (tag.getName().isEmpty()) {
                throw new ValidationException("Empty name!");
            }
        }
    }
}
