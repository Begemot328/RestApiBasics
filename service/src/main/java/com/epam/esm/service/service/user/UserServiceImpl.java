package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
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

    @Autowired
    public UserServiceImpl(UserDAO dao,
                           EntityValidator<User> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User create(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User read(int id) throws NotFoundException {
        Optional<User> userOptional = readOptional(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return userOptional.get();
        }
    }

    @Override
    public Optional<User> readOptional(int id) {
        return Optional.ofNullable(dao.read(id));
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
    public List<User> readAll() throws NotFoundException {
        List<User> users = dao.readAll();
        if (CollectionUtils.isEmpty(users)) {
            throw new NotFoundException("No users found!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return users;
        }
    }

    @Override
    public List<User> read(MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        UserFinder finder = new UserFinder(dao);
        for (String key : params.keySet()) {
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(params.get(key))) {
                throw new BadRequestException("Empty parameter!", ErrorCodes.USER_BAD_REQUEST);
            }
            try {
                if (PaginationParameters.contains(key)) {
                    parsePaginationParameter(finder, key, params.get(key));
                } else {
                    throw new BadRequestException("Unknown parameter!", ErrorCodes.USER_BAD_REQUEST);
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.USER_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterString, List<String> list) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(list.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(list.get(0)));
                break;
        }
    }

    private List<User> readBy(EntityFinder<User> entityFinder) throws NotFoundException {
        List<User> orders = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return orders;
        }
    }
}
