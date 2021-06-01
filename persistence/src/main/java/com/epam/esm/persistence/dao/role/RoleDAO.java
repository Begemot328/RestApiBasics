package com.epam.esm.persistence.dao.role;

import com.epam.esm.model.entity.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface RoleDAO extends CrudRepository<Role, Integer>, RoleDAOCustom {
    Role getByName(String name);
}
