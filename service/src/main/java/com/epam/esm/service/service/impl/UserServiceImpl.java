package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.UserDAO;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.UserService;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserDAO dao;
    private EntityValidator<User> validator;

    @Autowired
    public UserServiceImpl(UserDAO dao,
                          EntityValidator<User> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User create(User user) throws ServiceException, ValidationException {
        validator.validate(user);
        try {
            dao.create(user);
        } catch (DAOSQLException e) {
            throw new ServiceException(e, ErrorCodes.USER_INTERNAL_ERROR);
        }
        return user;
    }

    @Override
    public User read(int id) throws NotFoundException, BadRequestException {
        User user = dao.read(id);
        if (user == null) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.USER_NOT_FOUND);
        } else {
            return user;
        }
    }

    @Override
    public void delete(int id) throws BadRequestException {
        if (dao.read(id) == null) {
            throw new BadRequestException("Entity does not exist", ErrorCodes.ORDER_BAD_REQUEST);
        }
        dao.delete(id);
    }

    @Override
    public User update(User user) throws ValidationException {
        validator.validate(user);
        return dao.update(user);
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
    public List<User> readBy(EntityFinder<User> entityFinder) throws NotFoundException {
        return null;
    }
}
