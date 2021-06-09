package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.UserDAO;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PageableParameters;
import com.epam.esm.service.constants.UserSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link User} service implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDAO dao;
    private final EntityValidator<User> validator;
    private static final String NOT_FOUND_ERROR_MESSAGE = "Requested user not found(%s = %s)!";
    private static final String LOGIN_EXISTS_ERROR_MESSAGE = "Such login already exists! login= %s";

    @Autowired
    public UserServiceImpl(UserDAO dao,
                           EntityValidator<User> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User create(User user) throws ValidationException, BadRequestException {
        validator.validate(user);
        try {
            checkLoginIfVacant(user.getLogin());
            return dao.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private void checkLoginIfVacant(String login) throws BadRequestException {
        if (this.getByLogin(login).isPresent()) {
            throw new BadRequestException(String.format(LOGIN_EXISTS_ERROR_MESSAGE, login),
                    ErrorCodes.USER_BAD_REQUEST);
        }
    }

    @Override
    public User getById(int id) throws NotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, "id", id),
                        ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User user) {
        throw new UnsupportedOperationException();
    }

    @Lookup
    @Override
    public UserFinder getFinder() {
        return null;
    }

    @Override
    public List<User> findByParameters(MultiValueMap<String, String> params, Pageable pageable)
            throws NotFoundException, BadRequestException {
        UserFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                if (!PageableParameters.contains(entry.getKey())) {
                    parseFindParameter(finder, entry.getKey(), entry.getValue());
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.USER_BAD_REQUEST);
            }
        }
        return findByFinder(finder, pageable);
    }

    private void parseFindParameter(UserFinder finder, String parameterString, List<String> parameterValues) {
        UserSearchParameters parameter =
                UserSearchParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LOGIN:
                finder.findByLogin(parameterValues.get(0));
                break;
            case FIRST_NAME:
                finder.findByFirstName(decodeParam(parameterValues.get(0)));
                break;
            case LAST_NAME:
                finder.findByLastName(decodeParam(parameterValues.get(0)));
                break;
        }
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private List<User> findByFinder(UserFinder entityFinder, Pageable pageable) throws NotFoundException {
        List<User> orders = dao.findAll(
                entityFinder.getPredicate(), pageable).getContent();
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return dao.getByLogin(login);
    }
}
