package com.epam.esm.service.service.user;

import com.epam.esm.persistence.model.entity.User;
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
     * @return {@link Optional} of {@link User} with defined login.
     */
    Optional<User> getByLogin(String login);
}
