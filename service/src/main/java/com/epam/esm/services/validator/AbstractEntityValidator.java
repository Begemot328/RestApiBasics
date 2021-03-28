package com.epam.esm.services.validator;

import com.epam.esm.model.entity.Entity;

public abstract class AbstractEntityValidator<T extends Entity> implements EntityValidator<T>{
    private static final String INPUT_ERROR = "error.input";

}
