package com.epam.esm.persistence.dao.order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Order DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class OrderDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public OrderDAOImpl() {
        // Default constructor for Spring purposes.
    }
}
