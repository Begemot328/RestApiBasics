package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.model.entity.QRole;
import com.epam.esm.persistence.model.entity.Role;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface RoleDAO extends EntityRepository<Role, QRole, Integer> {

    /**
     * Find {@link Role} object by name.
     *
     * @return {@link Role} object.
     * @param name Name to find by.
     */
    Role getByName(String name);
}
