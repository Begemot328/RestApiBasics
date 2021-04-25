package com.epam.esm.persistence.util;

import com.epam.esm.model.entity.Entity;

import java.util.Objects;

/**
 * Abstract search criteria class to find {@link Entity} objects
 * via {@link com.epam.esm.persistence.dao.EntityDAO} datasources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public abstract class EntityFinder<T extends Entity> {
    protected String searchCondition = "";
    protected String sortCondition = "";
    protected String limit = "";
    protected String offset = "";

    public EntityFinder() {}

    /**
     * Search condition getter
     *
     * @return search condition
     */
    protected String getSearchCondition() {
        return searchCondition;
    }

    /**
     * Sort condition getter
     *
     * @return sort condition
     */
    protected String getSortCondition() {
        return sortCondition;
    }

    /**
     * Query getter
     *
     * @return query line
     */
    public String getQuery() {
        StringBuilder query = new StringBuilder();
        if (!searchCondition.isEmpty()) {
            query.append(FinderQueries.WHERE.getValue());
            query.append(" ").append(searchCondition);
        }
        if (!sortCondition.isEmpty()) {
            query.append(FinderQueries.ORDER_BY.getValue()).append(sortCondition);
        }
        if (!limit.isEmpty()) {
            query.append(FinderQueries.LIMIT.getValue()).append(limit);
        }
        if (!offset.isEmpty()) {
            query.append(FinderQueries.OFFSET.getValue()).append(offset);
        }
        return query.toString();
    }

    public void limit(int limit) {
        this.limit = Integer.toString(limit);
    }

    public void offset(int offset) {
        this.offset = Integer.toString(offset);
    }

    /**
     * Method to join another {@link EntityFinder} conditions by AND logic
     */
    public void and(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = searchCondition + " AND (" + finder.getSearchCondition() + ")";
        } else {
            this.searchCondition = finder.getSearchCondition();
        }
    }

    /**
     * Method to join another {@link EntityFinder} conditions by OR logic
     */
    public void or(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = searchCondition + " OR (" + finder.getSearchCondition() + ")";
        } else {
            this.searchCondition = finder.getSearchCondition();
        }
    }

    /**
     * Method to join another {@link EntityFinder} conditions by AND logic
     *
     * @param sorting name of the parameter to sort by
     * @param sortDirection {@link SortDirection} enum object to specify sorting order
     */
    public void sortBy(String sorting, SortDirection sortDirection) {
        if (!sortCondition.isEmpty()) {
            this.sortCondition = sortCondition.concat(", " + sorting + " " + sortDirection.toString());
        } else {
            this.sortCondition = sorting + " " + sortDirection.toString();
        }
    }

    /**
     * Method to find by query, adds it to previous condition according to AND logic by default
     *
     * @param query query to find by
     */
    protected void find(String query) {
        if (!searchCondition.isEmpty()) {
            this.searchCondition += " AND ";
        }
        this.searchCondition += query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityFinder<?> that = (EntityFinder<?>) o;
        return Objects.equals(getSearchCondition(), that.getSearchCondition()) && Objects.equals(getSortCondition(), that.getSortCondition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchCondition(), getSortCondition());
    }

    @Override
    public String toString() {
        return "EntityFinder{" +
                "searchCondition='" + searchCondition + '\'' +
                ", sortCondition='" + sortCondition + '\'' +
                "limit=" + limit +
                "offset=" + offset +
                '}';
    }
}
