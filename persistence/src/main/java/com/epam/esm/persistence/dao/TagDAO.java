package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagDAO extends EntityDAO<Tag> {
    /**
     * Find using {@link EntityFinder} method
     *
     * @param finder {@link EntityFinder} to obtain search parameters
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     * @return {@link java.util.ArrayList}  of {@link Tag} objects
     */
    List<Tag> readBy(EntityFinder<Tag> finder) throws DAOSQLException;
}
