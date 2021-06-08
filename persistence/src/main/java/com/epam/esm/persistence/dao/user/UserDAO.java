package com.epam.esm.persistence.dao.user;

import com.epam.esm.model.entity.QUser;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.EntityRepository;

import java.util.Optional;

/**
 * User DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface UserDAO extends EntityRepository<User, QUser, Integer> {

    /**
     * Find {@link User} object by login.
     *
     * @return {@link User} object concluded in {@link Optional}.
     * @param login Name to find by.
     */
    Optional<User> getByLogin(String login);
}
