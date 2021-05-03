package com.epam.esm.persistence.constants;

/**
 * Certificate column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum CertificateColumns {
    ID(0,"id"),
    DESCRIPTION(1,"description"),
    NAME(2,"name"),
    DURATION(3,"duration"),
    PRICE(4,"price"),
    TAG_NAME(5,"tag_name"),
    TAG_ID(6,"tag_id"),
    LAST_UPDATE_DATE(7,"last_update_date"),
    CREATE_DATE(8,"create_date");

    private final String value;
    private final int column;

    /**
     * Default constructor
     *
     * @param value column name
     */
    CertificateColumns(int column, String value) {
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
