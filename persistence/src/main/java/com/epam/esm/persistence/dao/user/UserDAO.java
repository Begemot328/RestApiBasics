package com.epam.esm.persistence.dao.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

public interface UserDAO extends EntityDAO<User> {

    String getPassword(String login);

    List<User> readBy(EntityFinder<User> finder);
}
