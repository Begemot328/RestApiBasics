package com.epam.esm.persistence.dao.role;

import com.epam.esm.model.entity.Role;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

/**
 * Role DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class RoleDAOImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public RoleDAOImpl() {
        // Default constructor for Spring purposes.
    }

    @Override
    public Role create(Role tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Role getById(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role update(Role tag) {
        throw new UnsupportedOperationException("Update operation for Role is unavailable");
    }

    @Override
    public void delete(int id) {
        entityManager.remove(getById(id));
    }

    @Override
    public List<Role> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = builder.createQuery(Role.class);
        Root<Role> rootEntry = query.from(Role.class);
        query = query.select(rootEntry);
        TypedQuery<Role> allQuery = entityManager.createQuery(query);
        return allQuery.getResultList();
    }

    @Override
    public CriteriaBuilder getBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }

    @Override
    public List<Role> findByParameters(EntityFinder<Role> finder) {
        return getAllQuery(finder).getResultList();
    }

    private TypedQuery<Role> getAllQuery(EntityFinder<Role> finder) {
        TypedQuery<Role> allQuery = entityManager.createQuery(finder.getQuery());
        allQuery.setFirstResult(finder.getOffset());
        if (finder.getLimit() > 0) {
            allQuery.setMaxResults(finder.getLimit());
        }
        return allQuery;
    }

    @Override
    public Role getByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = builder.createQuery(Role.class);
        Root<Role> rootEntry = query.from(Role.class);
        query = query.select(rootEntry);
        query.where(builder.equal(rootEntry.get(TagColumns.NAME.getValue()), name));
        TypedQuery<Role> allQuery = entityManager.createQuery(query);
        List<Role> roles = allQuery.getResultList();
        return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
    }
}
