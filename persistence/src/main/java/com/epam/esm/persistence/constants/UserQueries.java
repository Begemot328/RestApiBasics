package com.epam.esm.persistence.constants;

/**
 * Tag SQL queries enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum UserQueries {
    WHERE_ID(" WHERE id = ?"),
    SELECT_FROM_USER("SELECT * FROM certificates.user_account"),
    INSERT_USER("INSERT INTO certificates.user (first_name, last_name) VALUES (?, ?);"),
    INSERT_ACCOUNT("INSERT INTO certificates.account (login, password) VALUES (?, ?);"),
    UPDATE_USER("UPDATE certificates.user  SET first_name = ?, last_name = ? " +
            "WHERE id = ?;"),
    UPDATE_ACCOUNT("UPDATE certificates.account  SET login = ?, password = ? " +
            "WHERE id = ?;"),
    DELETE_USER("DELETE FROM certificates.user  " +
            "WHERE id = ?;"),
    SELECT_PASSWORD("SELECT password FROM account WHERE login = ?"),
    LIMIT(" LIMIT ?, ?");

    /**
     * Default constructor
     *
     * @param value column name
     */
    UserQueries(String value) {
        this.value = value;
    }

    private final String value;

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }
}
