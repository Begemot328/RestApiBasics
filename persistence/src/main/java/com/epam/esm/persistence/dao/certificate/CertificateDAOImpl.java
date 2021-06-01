package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Certificate DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class CertificateDAOImpl implements CertificateDAOCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public CertificateDAOImpl(){
        // Default constructor for Spring purposes.
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public List<Certificate> findByQuery(String query) {
        return entityManager.createQuery(query, Certificate.class).getResultList();
    }
}
