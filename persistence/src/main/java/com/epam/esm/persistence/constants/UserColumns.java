package com.epam.esm.persistence.constants;

/**
 * Tag column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum UserColumns {
    ID(1,"id"),
    FIRST_NAME(2,"first_name"),
    LAST_NAME(3, "last_name"),
    LOGIN(4, "login"),
    PASSWORD(5, "password");

    private String value;
    private int column;

    /**
     * Default constructor
     * @param value column name
     */
    UserColumns(int column, String value) {
        this.value = value;
        this.column = column;
    }

    /**
     * Value getter
     *
     * @return number of the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Value getter
     *
     * @return name of the column
     */
    public String getValue() {
        return value;
    }
}
