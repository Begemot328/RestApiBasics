package com.epam.esm.persistence.constants;

/**
 * Tag SQL queries enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum OrderQueries {
    WHERE_ID(" WHERE id = ?"),
    WHERE_CERTIFICATE_ID(" WHERE certificate_id = ?"),
    SELECT_FROM_ORDER("SELECT DISTINCT * FROM certificates.order_full"),
    INSERT_ORDER("INSERT INTO certificates.orders (user_id, certificate_id, purchase_date, amount, quantity) " +
            "VALUES (?, ?, ?, ?, ?);"),
    UPDATE_ORDER("UPDATE certificates.orders  SET user_id = ?, certificate_id = ?, " +
            "purchase_date = ?, amount = ?, quantity = ? " +
            "WHERE id = ?;"),
    DELETE_ORDER("DELETE FROM certificates.orders  " +
            "WHERE id = ?;"),
    LIMIT(" LIMIT ?, ?");

    private final String value;

    /**
     * Default constructor
     *
     * @param value column name
     */
    OrderQueries(String value) {
        this.value = value;
    }

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }
}
