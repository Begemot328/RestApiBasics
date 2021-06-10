package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * {@link User} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
@PropertySource("classpath:credentials.properties")
public class UserValidator implements EntityValidator<User> {

    @Value("${login.length}")
    private int loginMinimalLength;

    @Value("${password.length}")
    private int passwordMinimalLength;

    @Override
    public void validate(User user) throws ValidationException {
        validateFirstName(user);
        validateLastName(user);
        validateLogin(user);
        validatePassword(user);
        }

    private void validateFirstName(User user) throws ValidationException {
        validateNotEmptyString(user.getFirstName(), "User firstname",
                ErrorCodes.USER_VALIDATION_EXCEPTION);
    }
    private void validateLastName(User user) throws ValidationException {
        validateNotEmptyString(user.getLastName(), "User lastname",
                ErrorCodes.USER_VALIDATION_EXCEPTION);
    }
    private void validateLogin(User user) throws ValidationException {
        validateNotEmptyString(user.getLogin(), "User login",
                ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateSymbols(user.getLogin(), ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateMinimalLength(user.getLogin(), loginMinimalLength, ErrorCodes.USER_VALIDATION_EXCEPTION);
    }
    private void validatePassword(User user) throws ValidationException {
        validateNotEmptyString(user.getPassword(), "User password",
                ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateSymbols(user.getPassword(), ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateMinimalLength(user.getPassword(), passwordMinimalLength, ErrorCodes.USER_VALIDATION_EXCEPTION);
    }
}
