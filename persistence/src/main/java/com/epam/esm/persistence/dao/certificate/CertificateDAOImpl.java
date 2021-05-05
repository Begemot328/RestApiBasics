package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.util.finder.EntityFinder;
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
 * Certificate DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class CertificateDAOImpl implements CertificateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public CertificateDAOImpl(){
    }

    @Override
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public Certificate read(int id) {
        return entityManager.find(Certificate.class, id);
    }

    @Override
    public Certificate update(Certificate certificate) {
        return entityManager.merge(certificate);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(read(id));
    }

    @Override
    public List<Certificate> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        CriteriaQuery<Certificate> all = criteriaQuery.select(rootEntry);
        TypedQuery<Certificate> allQuery = entityManager.createQuery(all);
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
    public List<Certificate> readBy(EntityFinder<Certificate> finder) {
        TypedQuery<Certificate> allQuery = entityManager.createQuery(finder.getQuery());
        allQuery.setFirstResult(finder.getOffset());
        if(finder.getLimit() > 0) {
            allQuery.setMaxResults(finder.getLimit());
        }
        return allQuery.getResultList();
    }

    @Override
    public List<Certificate> readBy(String query) {
        return entityManager.createQuery(query, Certificate.class).getResultList();
    }
}
