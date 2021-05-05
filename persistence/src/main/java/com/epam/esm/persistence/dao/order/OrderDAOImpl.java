package com.epam.esm.persistence.dao.order;

import com.epam.esm.model.entity.Order;
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
 * Order DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class OrderDAOImpl implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public OrderDAOImpl() {
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Order read(int id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public Order update(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(read(id));
    }

    @Override
    public List<Order> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootEntry = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> all = criteriaQuery.select(rootEntry);
        TypedQuery<Order> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public List<Order> readBy(EntityFinder<Order> finder) {
        TypedQuery<Order> allQuery = entityManager.createQuery(finder.getQuery());
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
