package com.epam.esm.service.validator;


import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.exceptions.ValidationException;

public interface EntityValidator<T extends Entity> {

    void validate(T t) throws ValidationException;
}
