package com.epam.esm.persistence.dao.user;

import com.epam.esm.model.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface UserDAO extends CrudRepository<User, Integer>, UserDAOCustom {
}
