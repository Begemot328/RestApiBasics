package com.epam.esm.persistence.dao;


import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
     * Obtain {@link EntityManager} object for JPA.
     *
     * @return {@link java.util.ArrayList} of entities.
     */
    EntityManager getEntityManager();

    /**
     * Find all entities by corresponding {@link EntityFinder} method.
     *
     * @return {@link java.util.ArrayList} of entities.
     */
    default List<T> findByParameters(EntityFinder<T> finder) {
        return getTypedQuery(finder).getResultList();
    }

    /**
     * Get {@link TypedQuery} method. Auxiliary for {@link EntityFinder} methods.
     *
     * @return {@link java.util.ArrayList} of entities.
     */
    default TypedQuery<T> getTypedQuery(EntityFinder<T> finder) {
        TypedQuery<T> allQuery = getEntityManager().createQuery(finder.getQuery());
        allQuery.setFirstResult(finder.getOffset());
        if (finder.getLimit() > 0) {
            allQuery.setMaxResults(finder.getLimit());
        }
        return allQuery;
    }

    /**
     * Get {@link CriteriaBuilder} of the context.
     *
     * @return {@link CriteriaBuilder} of the context.
     */
    default CriteriaBuilder getBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    /**
     * Get {@link Metamodel} of the context.
     *
     * @return {@link Metamodel} of the context.
     */
    default Metamodel getMetamodel() {
        return getEntityManager().getMetamodel();
    }
}
