package com.epam.esm.persistence.dao.role;

import com.epam.esm.model.entity.Role;
import com.epam.esm.persistence.constants.TagColumns;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Role DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class RoleDAOImpl implements RoleDAOCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public RoleDAOImpl() {
        // Default constructor for Spring purposes.
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
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
