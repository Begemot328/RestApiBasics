package com.epam.esm.services.validator;


import com.epam.esm.model.entity.Entity;
import com.epam.esm.services.exceptions.ValidationException;

public interface EntityValidator<T extends Entity> {

    void validate(T t) throws ValidationException;
}
