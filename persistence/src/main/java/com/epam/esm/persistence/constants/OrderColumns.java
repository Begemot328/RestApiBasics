package com.epam.esm.persistence.constants;

/**
 * Tag column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum OrderColumns {
    ID(0,"id"),
    USER_ID(1,"user_id"),
    CERTIFICATE_ID(2, "certificate_id"),
    PURCHASE_DATE(3, "purchase_date"),
    AMOUNT(4, "amount"),
    QUANTITY(5, "quantity");

    private final String value;
    private final int column;

    /**
     * Default constructor
     * @param value column name
     */
    OrderColumns(int column, String value) {
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
