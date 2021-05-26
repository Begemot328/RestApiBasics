package com.epam.esm.persistence.constants;

/**
 * Tag SQL queries enum.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum TagQueries {
    SELECT_MOST_POPULAR_TAG("SELECT count(tag_id), name, t.id\n" +
            "FROM certificates.orders\n" +
            " LEFT JOIN certificates.certificate_tag ct ON orders.certificate_id = ct.certificate_id\n" +
            " LEFT JOIN certificates.tag t ON t.id = ct.tag_id\n" +
            " JOIN (SELECT user_id, amount_sum\n" +
            " FROM (SELECT user_id, sum(amount) AS amount_sum\n" +
            " FROM certificates.orders\n" +
            " GROUP BY user_id) AS t2\n" +
            " ORDER BY amount_sum DESC\n" +
            " LIMIT 1) AS t1 ON t1.user_id = orders.user_id\n" +
            " GROUP BY tag_id\n" +
            " ORDER BY COUNT(tag_id) DESC\n" +
            " LIMIT 1;"),
    SELECT_FROM_TAG("SELECT DISTINCT * FROM certificates.tag"),
    SELECT_FROM_TAG_CERTIFICATES("SELECT DISTINCT id, name FROM certificates.tag_certificates"),
    INSERT_TAG("INSERT INTO certificates.tag (name) VALUES (?);"),
    DELETE_TAG("DELETE FROM certificates.tag  " +
            "WHERE id = ?;");

    private final String value;

    /**
     * Default constructor.
     *
     * @param value Column name.
     */
    TagQueries(String value) {
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
