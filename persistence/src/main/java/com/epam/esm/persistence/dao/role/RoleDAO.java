package com.epam.esm.persistence.dao.role;

import com.epam.esm.model.entity.Role;
import com.epam.esm.persistence.dao.EntityDAO;

import java.util.List;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface RoleDAO extends EntityDAO<Role> {

    Role getByName(String name);
}
