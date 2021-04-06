package com.epam.esm.persistence.constants;

/**
 * Tag column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum TagColumns {
    ID("id"),
    NAME("name"),
    CERTIFICATE_ID( "certificate_id");

    /**
     * Default constructor
     * @param value column name
     */
    TagColumns(String value) {
        this.value = value;
    }

    private String value;

    /**
     * Value getter
     *
     * @return name of the column
     */
    public String getValue() {
        return value;
    }
}
