package com.epam.esm.persistence.util;

/**
 * Querries for {@link EntityFinder} object
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum FinderQueries {
    WHERE(" WHERE "),
    ORDER_BY(" ORDER BY "),
    LIKE("LIKE '%VALUE%'"),
    VALUE("VALUE");

    /**
     * Default constructor
     * @param value column name
     */
    FinderQueries(String value) {
        this.value = value;
    }

    private String value;

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }
}
