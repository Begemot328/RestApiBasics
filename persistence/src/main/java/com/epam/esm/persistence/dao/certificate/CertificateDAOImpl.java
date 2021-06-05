package com.epam.esm.persistence.dao.certificate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Certificate DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class CertificateDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public CertificateDAOImpl(){
        // Default constructor for Spring purposes.
    }
}
