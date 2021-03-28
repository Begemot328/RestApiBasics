package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Entity;

public abstract class EntityFinder<T extends Entity> {
    protected static final String WHERE = " WHERE";
    protected static final String ORDER_BY = " ORDER BY";
    protected static final String BLANK = " ";
    protected static final String AND = "AND";
    protected static final String OR = "OR";
    protected static final String LIKE = "LIKE '%VALUE%'";
    protected static final String IN = "IN (VALUE)";
    protected static final String VALUE = "VALUE";
    protected static final String EQUALS = "EQUALS";
    protected static final String MORE = ">";
    protected static final String LESS = "<";
    protected static final String MORE_OE = ">=";
    protected static final String LESS_OE = "<=";
    protected static final String DELIMETER = ",";
    protected static final String SEMICOLON = ";";
    protected static final String QUOTE = "'";

    protected String searchCondition = new String();
    protected String sortCondition = new String();

    public EntityFinder() {

    }

    protected String getSearchCondition() {
        return searchCondition;
    }

    protected String getSortCondition() {
        return sortCondition;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder(WHERE);
        query.append(BLANK).append(searchCondition);
        if (!sortCondition.isEmpty()) {
            query.append(BLANK).append(ORDER_BY).append(BLANK).append(sortCondition);
        }
        return query.toString();
    }

    public void and(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = sortCondition + BLANK + AND + BLANK
                    + "(" + finder.getSearchCondition() + ")";
        }
    }

    public void or(EntityFinder<T> finder) {
        if (!finder.getSearchCondition().isEmpty()) {
            this.searchCondition = sortCondition + BLANK + OR + BLANK
                    + "(" + finder.getSearchCondition() + ")";
        }
    }

    public void sortById(AscDesc ascDesc) {
        sortBy(AbstractEntityDAO.ID, ascDesc);
    }

    protected void sortBy(String sorting) {
        if (!sortCondition.isEmpty()) {
            this.sortCondition = sortCondition.concat(DELIMETER).concat(BLANK).concat(sorting);
        } else {
            this.sortCondition = sorting;
        }
    }

    protected void sortBy(String sorting, AscDesc ascDesc) {
        if (!sortCondition.isEmpty()) {
            this.sortCondition = sortCondition.concat(DELIMETER).concat(BLANK).concat(sorting)
                    .concat(BLANK).concat(ascDesc.toString());
        } else {
            this.sortCondition = sorting.concat(BLANK).concat(ascDesc.toString());
        }
    }
}
