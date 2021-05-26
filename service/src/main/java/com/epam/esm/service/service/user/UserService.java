package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.EntityService;

import java.util.Optional;

/**
 * {@link User} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface UserService extends EntityService<User> {

    /**
     * Find {@link User} by his unique login.
     *
     * @param login Login value.
     * @return {@link User} with defined login.
     */
    User getByUniqueLogin(String login) throws NotFoundException;

    Optional<User> getByUniqueLoginOptional(String login);
}
