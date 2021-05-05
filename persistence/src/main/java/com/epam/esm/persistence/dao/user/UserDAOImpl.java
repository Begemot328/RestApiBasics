package com.epam.esm.persistence.dao.user;

import com.epam.esm.model.entity.User;
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
 * User DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public UserDAOImpl() {
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User read(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(read(id));
    }

    @Override
    public List<User> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> rootEntry = criteriaQuery.from(User.class);
        CriteriaQuery<User> all = criteriaQuery.select(rootEntry);
        TypedQuery<User> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public List<User> readBy(EntityFinder<User> finder) {
        TypedQuery<User> allQuery = entityManager.createQuery(finder.getQuery());
        allQuery.setFirstResult(finder.getOffset());
        if(finder.getLimit() > 0) {
            allQuery.setMaxResults(finder.getLimit());
        }
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
}
