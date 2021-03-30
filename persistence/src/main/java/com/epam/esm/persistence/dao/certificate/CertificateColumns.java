package com.epam.esm.persistence.dao.certificate;

public enum CertificateColumns {
    DESCRIPTION("description"),
    NAME("name"),
    DURATION("duration"),
    PRICE("price"),
    TAG_NAME("tag_name"),
    ID("id"),
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
