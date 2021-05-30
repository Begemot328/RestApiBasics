package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.SortDirection;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.constants.UserSortingParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.DataIntegrityViolationException;
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

    private String loginExistsErrorMessage = "Such login already exists! login= %s";

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
            validateUserLogin(user.getLogin());
            return dao.create(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private void validateUserLogin(String login) throws BadRequestException {
        if (this.getByLogin(login).isPresent()) {
            throw new BadRequestException(String.format(loginExistsErrorMessage, login),
                    ErrorCodes.USER_BAD_REQUEST);
        }
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(dao.getById(id));
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
        List<User> users = dao.findAll();
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
    public List<User> findByParameters(MultiValueMap<String, String> params)
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
        return readBy(finder);
    }

    private void parseParameter(UserFinder finder, String parameterName, List<String> parameterValues)
            throws BadRequestException {
        if (parameterName.contains("sort")) {
            parseSortParameter(finder, parameterName, parameterValues);
        } else if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, parameterValues);
        } else {
            throw new BadRequestException("Unknown parameter!", ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private void parseSortParameter(UserFinder finder, String parameterName, List<String> parameterValues) {
        finder.sortBy(UserSortingParameters.getEntryByParameter(parameterName).getValue(),
                SortDirection.parseAscDesc(parameterValues.get(0)));
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterName, List<String> parameterValues) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(parameterValues.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(parameterValues.get(0)));
                break;
        }
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private List<User> readBy(EntityFinder<User> entityFinder) throws NotFoundException {
        List<User> orders = dao.findByParameters(entityFinder);
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        UserFinder finder = getFinder();
        addToFinder(finder::findByLogin, login);
        List<User> users = dao.findByParameters(finder);
        if (CollectionUtils.isEmpty(users)) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }
}
