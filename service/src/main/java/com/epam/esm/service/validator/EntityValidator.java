package com.epam.esm.service.validator;


import com.epam.esm.model.entity.Entity;
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
     */
    void validate(T t) throws ValidationException;

    default void validateNotEmptyString(String param, String name, int errorCode) throws ValidationException {
        if (param == null) {
            throw new ValidationException(name + " can't be null!",
                    errorCode);
        }
        if (param.isEmpty()) {
            throw new ValidationException(name + " can't be empty!",
                    errorCode);
        }
    }

    default void validateNotNullObject(Object object, String name, int errorCode) throws ValidationException {
        if (object == null) {
            throw new ValidationException(name + " can't be null!",
                    errorCode);
        }
    }

    default void validatePositiveNumber(Number number, String name, int errorCode) throws ValidationException {
        if (number == null) {
            throw new ValidationException(name + " can't be null!",
                    errorCode);
        }
        if (number.doubleValue() <= 0) {
            throw new ValidationException(name + " can't non-positive!",
                    errorCode);
        }
    }
}
