package com.epam.esm.persistence.util.finder;

import com.epam.esm.persistence.model.entity.CustomEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.context.annotation.Scope;
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
    protected BooleanExpression expression;

    /**
     * Constructor.
     *
     */
    protected EntityFinder() {
        expression = Expressions.asBoolean(true).isTrue();
    }

    public Predicate getPredicate() {
        return expression;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityFinder<?> that = (EntityFinder<?>) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
