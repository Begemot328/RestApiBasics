package com.epam.esm.persistence.constants;

public enum CertificateColumns {
    ID("id"),
    DESCRIPTION("description"),
    NAME("name"),
    DURATION("duration"),
    PRICE("price"),
    TAG_NAME("tag_name"),
    TAG_ID("tag_id"),
    LAST_UPDATE_DATE("last_update_date"),
    CREATE_DATE ("create_date");

    CertificateColumns(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
