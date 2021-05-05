package com.epam.esm.persistence.dao;


import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.persistence.util.finder.EntityFinder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

/**
 * Entity DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityDAO<T extends CustomEntity> {

    /**
     * Create method.
     *
     * @param t Entity to create.
     */
    T create(T t);

    /**
     * Read method.
     *
     * @param id ID of the entity.
     */
    T read(int id);

    /**
     * Update method.
     *
     * @param t entity to create.
     * @return Updated {@link Certificate}.
     */
    T update(T t);

    /**
     * Delete method.
     *
     * @param id ID of the entity to delete.
     */
    void delete(int id);

    /**
     * Find all method.
     *
     * @return {@link java.util.ArrayList} of entities.
     */
    List<T> readAll();

    /**
     * Get {@link CriteriaBuilder} of the context.
     *
     * @return {@link CriteriaBuilder} of the context.
     */
    CriteriaBuilder getBuilder();

    /**
     * Get {@link Metamodel} of the context.
     *
     * @return {@link Metamodel} of the context.
     */
    Metamodel getMetamodel();

    /**
     * Find all entities by corresponding {@link EntityFinder} method.
     *
     * @return {@link java.util.ArrayList} of entities.
     */
    List<T> readBy(EntityFinder<T> finder);
}
