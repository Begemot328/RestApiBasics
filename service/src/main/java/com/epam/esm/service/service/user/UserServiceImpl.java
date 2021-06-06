package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAO;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.constants.UserSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.function.Consumer;

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

    @Override
    public List<User> findAll() throws NotFoundException {
        List<User> users = IterableUtils.toList(dao.findAll());
        if (CollectionUtils.isEmpty(users)) {
            throw new NotFoundException("No users found!",
                    ErrorCodes.USER_NOT_FOUND);
        }
        return users;
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
                parseParameter(finder, entry.getKey(), entry.getValue());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.USER_BAD_REQUEST);
            }
        }
        return findByFinder(finder, pageable);
    }

    private void parseParameter(UserFinder finder, String parameterName, List<String> parameterValues) {
        if (parameterName.equals(SORT) || PaginationParameters.contains(parameterName)) {
            return;
        } else {
            parseFindParameter (finder, parameterName, parameterValues);
        }
    }

    private void parseFindParameter(UserFinder finder, String parameterString, List<String> parameterValues) {
        UserSearchParameters parameter =
                UserSearchParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LOGIN:
                addToFinder(finder::findByLogin, parameterValues);
                break;
            case FIRST_NAME:
                addToFinder(finder::findByFirstName, parameterValues);
                break;
            case LAST_NAME:
                addToFinder(finder::findByLastName, parameterValues);
                break;
        }
    }

    private void addToFinder(Consumer<String> consumer, List<String> parameterValues) {
        if (CollectionUtils.isNotEmpty(parameterValues)) {
            addToFinder(consumer, parameterValues.get(0));
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

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }
}
