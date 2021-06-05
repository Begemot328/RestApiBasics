package com.epam.esm.persistence.util.finder;

import com.epam.esm.model.entity.CustomEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Abstract search criteria class to find {@link CustomEntity} objects.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
@Scope("prototype")
public abstract class EntityFinder<T extends CustomEntity> {
    private static final int DEFAULT_LIMIT = 20;
    protected int limit = DEFAULT_LIMIT;
    protected int page = 0;
    protected Pageable paginationAndSorting;
    protected Sort sort;
    protected BooleanExpression expression;

    /**
     * Constructor.
     *
     */
    protected EntityFinder() {
        expression = Expressions.asBoolean(true).isTrue();
        sort = Sort.unsorted();
    }

    public Predicate getPredicate() {
        return expression;
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
     * @param page Page number to search.
     */
    public void page(int page) {
        this.page = page;
    }

    /**
     * Method to join another {@link EntityFinder} conditions by AND logic.
     *
     * @param finder {@link EntityFinder} to add, corresponding by type.
     */
    public void and(EntityFinder<T> finder) {
        expression.and(finder.getPredicate());
    }

    /**
     * Method to join another {@link Predicate} conditions by AND logic.
     *
     * @param predicate {@link Predicate} to add, corresponding by type.
     */
    public void and(Predicate predicate) {
        expression = expression.and(predicate);
    }

    /**
     * Method to join another {@link Predicate} conditions by OR logic.
     *
     * @param predicate {@link Predicate} to add, corresponding by type.
     */
    protected void or(Predicate predicate) {
        expression = expression.or(predicate);
    }

    /**
     * Method to join another {@link EntityFinder} conditions by OR logic
     *
     * @param finder {@link EntityFinder} to add, corresponding by type.
     */
    public void or(EntityFinder<T> finder) {
        or(finder.getPredicate());
    }

    /**
     * Method to join another {@link BooleanExpression} conditions by AND logic (default behaviour).
     *
     * @param predicate {@link BooleanExpression} to add, corresponding by type.
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
        sort = sort.and(Sort.by(sorting));
        if (sortDirection == SortDirection.DESC) {
            sort = sort.descending();
        }
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
    public int getPage() {
        return page;
    }

    public Pageable getPaginationAndSorting() {
        return PageRequest.of(page, limit, sort);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityFinder<?> that = (EntityFinder<?>) o;
        return limit == that.limit && page == that.page && Objects.equals(paginationAndSorting, that.paginationAndSorting) && Objects.equals(sort, that.sort) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, page, paginationAndSorting, sort, expression);
    }
}
