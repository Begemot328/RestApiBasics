package com.epam.esm.services.validator;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class TagValidator extends AbstractEntityValidator<Tag>{
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
