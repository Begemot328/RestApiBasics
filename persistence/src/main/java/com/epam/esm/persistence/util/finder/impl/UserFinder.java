package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.UserColumns;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link User} objects.
 * via {@link com.epam.esm.persistence.dao.user.UserDAO} data sources.
 *
 * @author Yury Zmushko
 * @version 2.0
 */

@Component
@Scope("prototype")
public class UserFinder extends EntityFinder<User> {

    /**
     * Constructor
     *
     * @param dao {@link EntityDAO} object to obtain {@link javax.persistence.criteria.CriteriaBuilder}
     *                             and {@link javax.persistence.metamodel.Metamodel objects}
     */
    public UserFinder(EntityDAO<User> dao) {
        super(dao);
    }

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }

    /**
     * Find by login condition adding method.
     *
     * @param login String that found login will include.
     */
    public void findByLogin(String login) {
        add(builder.equal(root.get(UserColumns.LOGIN.getValue()), login));
    }
}
