package com.epam.esm.service.validator;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
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
public class UserValidator implements EntityValidator<User> {

    @Override
    public void validate(User user) throws ValidationException {
        validateNotEmptyString(user.getFirstName(), "User firstname", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateNotEmptyString(user.getLastName(), "User lastname", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateNotEmptyString(user.getLogin(), "User login", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateNotEmptyString(user.getPassword(), "User password", ErrorCodes.USER_VALIDATION_EXCEPTION);
    }
}
