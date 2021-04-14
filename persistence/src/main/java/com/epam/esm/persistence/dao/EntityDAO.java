package com.epam.esm.persistence.dao;


import com.epam.esm.model.entity.Certificate;
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
     */
    T create(T t) throws DAOSQLException;

    /**
     * Read method
     *
     * @param id of the entity
     *
     */
    T read(int id);

    /**
     * Update method
     *
     * @param t entity to create
     *
     * @return  updated {@link Certificate}
     */
    Certificate update(T t);

    /**
     * Delete method
     *
     * @param id of the entity to delete
     *
     */
    void delete(int id);

    /**
     * Find all method
     *
     * @return {@link java.util.ArrayList} of entities
     */
    List<T> readAll();
}
