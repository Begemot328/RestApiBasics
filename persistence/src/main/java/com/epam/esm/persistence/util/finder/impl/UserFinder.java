package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.persistence.dao.UserDAO;
import com.epam.esm.persistence.model.entity.QUser;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link User} objects.
 * via {@link UserDAO} data sources.
 *
 * @author Yury Zmushko
 * @version 2.0
 */

@Component
@Scope("prototype")
public class UserFinder extends EntityFinder<User> {

    /**
     * Constructor
     **/
    public UserFinder() {
        super();
    }

    /**
     * Find by firstname condition adding method.
     *
     * @param name String that found firstname must include.
     */
    public void findByFirstName(String name) {
        add(QUser.user.firstName.contains(name));
    }

    /**
     * Find by lastname condition adding method.
     *
     * @param name String that found lastname must include.
     */
    public void findByLastName(String name) {
        add(QUser.user.lastName.containsIgnoreCase(name));
    }

    /**
     * Find by login condition adding method.
     *
     * @param login String that found login must include.
     */
    public void findByLogin(String login) {
        add(QUser.user.login.like(login));
    }
}
