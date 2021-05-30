package com.epam.esm.service.validator;


import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.service.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Validation interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityValidator<T extends CustomEntity> {

    /**
     * Validate {@link CustomEntity} method.
     *
     * @param t {@link CustomEntity} to validate.
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

    default void validateSymbols(String s, int errorCode) throws ValidationException {
        Pattern pattern = Pattern.compile("[A-Za-z0-9!?.,;:@$-_]*");
        if (!pattern.matcher(s).matches()) {
            throw new ValidationException(
                    "String must contain only, letters, digits and !?.,;:@$-_ can't be null!", errorCode);
        }
    }

    default void validateMinimalLength(String s, int length, int errorCode) throws ValidationException {
        if (s.length() < length) {
            throw new ValidationException(String.format(
                    "Must be at least %s charachters length!", length), errorCode);
        }
    }
}
