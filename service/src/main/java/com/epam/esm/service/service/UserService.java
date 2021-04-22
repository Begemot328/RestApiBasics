package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * {@link Tag} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface UserService extends EntityService<User> {


    List<User> read(Map<String, String> params) throws BadRequestException, NotFoundException;

    User signIn(String login) throws BadRequestException, NotFoundException;
}
