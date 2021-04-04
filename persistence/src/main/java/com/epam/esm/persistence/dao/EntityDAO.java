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
    public static final String ID = "id";

    /**
     * Create method
     *
     * @param t entity to create
     *
     * @throws DAOSQLException
     */
    T create(T t) throws DAOSQLException;

    /**
     * Read method
     *
     * @param id of the entity
     *
     * @throws DAOSQLException
     */
    T read(int id) throws DAOSQLException;

    /**
     * Update method
     *
     * @param t entity to create
     *
     * @throws DAOSQLException
     */
    void update(T t) throws DAOSQLException;

    /**
     * Delete method
     *
     * @param id of the entity to delete
     *
     * @throws DAOSQLException
     */
    void delete(int id) throws DAOSQLException;

    /**
     * Find all method
     *
     * @throws DAOSQLException
     * @return
     */
    List<T> findAll() throws DAOSQLException;
}
