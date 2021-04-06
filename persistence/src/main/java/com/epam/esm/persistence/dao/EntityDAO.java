package com.epam.esm.persistence.dao;


import com.epam.esm.model.entity.Entity;
import com.epam.esm.persistence.exceptions.DAOSQLException;

import java.util.List;

/**
 * Entity DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityDAO<T extends Entity> {

    /**
     * Create method
     *
     * @param t entity to create
     *
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     */
    T create(T t) throws DAOSQLException;

    /**
     * Read method
     *
     * @param id of the entity
     *
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     */
    T read(int id) throws DAOSQLException;

    /**
     * Update method
     *
     * @param t entity to create
     *
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     */
    void update(T t) throws DAOSQLException;

    /**
     * Delete method
     *
     * @param id of the entity to delete
     *
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     */
    void delete(int id) throws DAOSQLException;

    /**
     * Find all method
     *
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     * @return {@link java.util.ArrayList} of entities
     */
    List<T> findAll() throws DAOSQLException;
}
