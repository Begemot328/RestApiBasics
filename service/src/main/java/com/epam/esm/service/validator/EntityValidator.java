package com.epam.esm.service.validator;


import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.exceptions.ValidationException;

/**
 * Validation interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityValidator<T extends Entity> {

    /**
     * Validate {@link Entity} method
     *
     * @param t {@link Entity} to validate
     *
     */
    void validate(T t) throws ValidationException;
}
