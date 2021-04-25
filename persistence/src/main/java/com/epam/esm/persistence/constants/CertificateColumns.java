package com.epam.esm.persistence.constants;

/**
 * Certificate column names enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum CertificateColumns {
    ID("id"),
    DESCRIPTION("description"),
    NAME("name"),
    DURATION("duration"),
    PRICE("price"),
    TAG_NAME("tag_name"),
    TAG_ID("tag_id"),
    LAST_UPDATE_DATE("last_update_date"),
    CREATE_DATE("create_date");

    private String value;

    /**
     * Default constructor
     *
     * @param value column name
     */
    CertificateColumns(String value) {
        this.value = value;
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
