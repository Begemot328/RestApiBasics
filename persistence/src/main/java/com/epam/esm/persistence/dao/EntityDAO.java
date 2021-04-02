package com.epam.esm.persistence.dao;


import com.epam.esm.model.entity.Entity;
import com.epam.esm.persistence.exceptions.DAOException;

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
     * @throws DAOException
     */
    T create(T t) throws DAOException;

    /**
     * Read method
     *
     * @param id of the entity
     *
     * @throws DAOException
     */
    T read(int id) throws DAOException;

    /**
     * Update method
     *
     * @param t entity to create
     *
     * @throws DAOException
     */
    void update(T t) throws DAOException;

    /**
     * Delete method
     *
     * @param id of the entity to delete
     *
     * @throws DAOException
     */
    void delete(int id) throws DAOException;

    /**
     * Find all method
     *
     * @throws DAOException
     * @return
     */
    List<T> findAll() throws DAOException;
}
