package com.epam.esm.persistence.constants;

/**
 * Tag SQL queries enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum TagQueries {
    WHERE_ID(" WHERE id = ?"),
    SELECT_MOST_POPULAR_TAG("SELECT count(tag_id), name, t.id\n" +
            "FROM orders\n" +
            " LEFT JOIN certificate_tag ct ON orders.certificate_id = ct.certificate_id\n" +
            " LEFT JOIN tag t ON t.id = ct.tag_id\n" +
            " JOIN (SELECT user_id, amount_sum\n" +
            " FROM (SELECT user_id, sum(amount) AS amount_sum\n" +
            " FROM orders\n" +
            " GROUP BY user_id) AS t\n" +
            " ORDER BY amount_sum DESC\n" +
            " LIMIT 1) AS t1 ON t1.user_id = orders.user_id\n" +
            " GROUP BY tag_id\n" +
            " ORDER BY COUNT(tag_id) DESC\n" +
            " LIMIT 1;"),
    WHERE_CERTIFICATE_ID(" WHERE certificate_id = ?"),
    SELECT_FROM_TAG("SELECT * FROM certificates.tag"),
    SELECT_FROM_TAG_CERTIFICATES("SELECT * FROM certificates.tag_certificates"),
    INSERT_TAG("INSERT INTO certificates.tag (name) VALUES (?);"),
    UPDATE_TAG("UPDATE certificates.tag  SET name = ? " +
            "WHERE id = ?;"),
    DELETE_TAG("DELETE FROM certificates.tag  " +
            "WHERE id = ?;"),
    LIMIT(" LIMIT ?, ?");

    private String value;

    /**
     * Default constructor
     *
     * @param value column name
     */
    TagQueries(String value) {
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
