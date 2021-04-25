package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

public interface UserDAO extends EntityDAO<User> {

    String getPassword(String login);

    List<User> readBy(EntityFinder<User> finder);
}
