package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

public interface TagDAO extends EntityDAO<Tag> {
    List<Tag> findBy(EntityFinder<Tag> finder) throws DAOSQLException;
}
