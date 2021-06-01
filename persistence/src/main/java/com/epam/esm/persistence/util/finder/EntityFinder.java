package com.epam.esm.persistence.util.finder;

import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.persistence.dao.EntityDAO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract search criteria class to find {@link CustomEntity} objects.
 * via {@link com.epam.esm.persistence.dao.EntityDAO} data sources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
@Scope("prototype")
public abstract class EntityFinder<T extends CustomEntity> {
    private static final int DEFAULT_LIMIT = 20;
    protected int limit = DEFAULT_LIMIT;
    protected int offset = 0;
    protected CriteriaQuery<T> query;
    protected CriteriaBuilder builder;
    protected Predicate predicate;
    protected Root<T> root;
    protected Metamodel metamodel;

    /**
     * Constructor.
     *
     * @param dao {@link EntityDAO } correspondind to finder class.
     */
    protected EntityFinder(EntityDAO<T> dao) {
        builder = dao.getBuilder();
        metamodel = dao.getMetamodel();
        query = builder.createQuery(getClassType());
        root = query.from(getClassType());
    }

    /**
     * Class type getter.
     *
     * @return Type of the class to search for.
     */
    protected abstract Class<T> getClassType();

    /**
     * Query getter.
     *
     * @return Query object.
     */
    public CriteriaQuery<T> getQuery() {
        if(Objects.nonNull(predicate)) {
            query.where(predicate);
        }
        query.select(root);
        return query;
    }

    /**
     * Method to define limit of the search.
     *
     * @param limit Limit of the search.
     */
    public void limit(int limit) {
        this.limit = limit;
    }

    /**
     * Method to define offset of the search.
     *
     * @param offset Offset of the search.
     */
    public void offset(int offset) {
        this.offset = offset;
    }

    /**
     * Method to join another {@link EntityFinder} conditions by AND logic.
     *
     * @param finder {@link EntityFinder} to add, corresponding by type.
     */
    public void and(EntityFinder<T> finder) {
        and(finder.getQuery().getRestriction());
    }

    /**
     * Method to join another {@link Predicate} conditions by AND logic.
     *
     * @param predicate {@link Predicate} to add, corresponding by type.
     */
    protected void and(Predicate predicate) {
        if(this.predicate == null) {
            this.predicate = predicate;
        } else {
            this.predicate = builder.and(predicate, this.predicate);
        }
    }

    /**
     * Method to join another {@link Predicate} conditions by OR logic.
     *
     * @param predicate {@link Predicate} to add, corresponding by type.
     */
    protected void or(Predicate predicate) {
        if(this.predicate == null) {
            this.predicate = predicate;
        } else {
            this.predicate = builder.or(predicate, this.predicate);
        }
    }

    /**
     * Method to join another {@link EntityFinder} conditions by OR logic
     *
     * @param finder {@link EntityFinder} to add, corresponding by type.
     */
    public void or(EntityFinder<T> finder) {
        or(finder.getQuery().getRestriction());
    }

    /**
     * Method to join another {@link Predicate} conditions by AND logic (default behaviour).
     *
     * @param predicate {@link Predicate} to add, corresponding by type.
     */
    protected void add(Predicate predicate) {
        and(predicate);
    }

    /**
     * Method to join sorting conditions.
     *
     * @param sorting       Name of the parameter to sort by.
     * @param sortDirection {@link SortDirection} enum object to specify sorting order.
     */
    public void sortBy(String sorting, SortDirection sortDirection) {
        List<Order> orderList = CollectionUtils.isEmpty(query.getOrderList())
                ? new ArrayList<>()
                        : query.getOrderList();
        if (sortDirection == SortDirection.DESC) {
            orderList.add(builder.desc(root.get(sorting)));
        } else {
            orderList.add(builder.asc(root.get(sorting)));
        }
        query.orderBy(orderList);
    }

    /**
     * Limit value getter.
     *
     * @return Limit value.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Offset value getter.
     *
     * @return Offset value.
     */
    public int getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityFinder<?> that = (EntityFinder<?>) o;
        return limit == that.limit && offset == that.offset
                && Objects.equals(query, that.query)
                && Objects.equals(builder, that.builder)
                && Objects.equals(predicate, that.predicate)
                && Objects.equals(root, that.root)
                && Objects.equals(metamodel, that.metamodel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset, query, builder, predicate, root, metamodel);
    }
}
