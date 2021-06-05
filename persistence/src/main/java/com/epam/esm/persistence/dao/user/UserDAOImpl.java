package com.epam.esm.persistence.dao.user;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class UserDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public UserDAOImpl() {
        // Default constructor for Spring purposes.
    }
}
