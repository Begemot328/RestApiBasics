package com.epam.esm.persistence.dao.role;

import com.epam.esm.model.entity.QRole;
import com.epam.esm.model.entity.Role;
import com.epam.esm.persistence.dao.EntityRepository;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface RoleDAO extends EntityRepository<Role, QRole, Integer> {
    Role getByName(String name);
}
