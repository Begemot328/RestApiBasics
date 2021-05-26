package com.epam.esm.persistence.util.finder;

/**
 * Querries for {@link EntityFinder} object enum.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum FinderQueries {
    WHERE(" WHERE "),
    LIMIT(" LIMIT "),
    OFFSET(" OFFSET "),
    ORDER_BY(" ORDER BY "),
    LIKE("LIKE '%VALUE%'"),
    VALUE("VALUE");

    private final String value;

    /**
     * Constructor.
     *
     * @param value column name
     */
    FinderQueries(String value) {
        this.value = value;
    }

    /**
     * Value getter.
     *
     * @return Query String line.
     */
    public String getValue() {
        return value;
    }
}
