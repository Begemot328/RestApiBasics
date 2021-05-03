package com.epam.esm.persistence.constants;

/**
 * Tag column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum TagColumns {
    ID(0,"id"),
    NAME(1,"name"),
    CERTIFICATE_ID(2,"certificate_id");

    private final String value;
    private final int column;

    /**
     * Default constructor
     *
     * @param value column name
     */
    TagColumns(int column, String value) {
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
