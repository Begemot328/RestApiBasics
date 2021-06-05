package com.epam.esm.persistence.dao.role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Role DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class RoleDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public RoleDAOImpl() {
        // Default constructor for Spring purposes.
    }
}
