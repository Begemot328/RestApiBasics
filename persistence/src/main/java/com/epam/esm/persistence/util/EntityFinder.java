package com.epam.esm.persistence.util;

import com.epam.esm.model.entity.Entity;

import java.util.Objects;


public abstract class EntityFinder<T extends Entity> {
    protected String searchCondition = "";
    protected String sortCondition = "";

    public EntityFinder() {}

    protected String getSearchCondition() {
        return searchCondition;
    }

    protected String getSortCondition() {
        return sortCondition;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder();
        if (!searchCondition.isEmpty()) {
            query.append(FinderQueries.WHERE.getValue());
            query.append(" ").append(searchCondition);
        }
        if (!sortCondition.isEmpty()) {
            query.append(FinderQueries.ORDER_BY.getValue()).append(sortCondition);
        }
        return query.toString();
    }

    public void and(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = searchCondition + " AND (" + finder.getSearchCondition() + ")";
        } else {
            this.searchCondition = finder.getSearchCondition();
        }
    }

    public void or(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = searchCondition + " OR (" + finder.getSearchCondition() + ")";
        } else {
            this.searchCondition = finder.getSearchCondition();
        }
    }

    public void sortBy(String sorting, AscDesc ascDesc) {
        if (!sortCondition.isEmpty()) {
            this.sortCondition = sortCondition.concat(", " + sorting + " " + ascDesc.toString());
        } else {
            this.sortCondition = sorting + " " + ascDesc.toString();
        }
    }

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
                '}';
    }
}
