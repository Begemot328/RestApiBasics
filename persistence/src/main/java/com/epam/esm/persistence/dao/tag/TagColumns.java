package com.epam.esm.persistence.dao.tag;

public enum TagColumns {
    NAME("name"),
    CERTIFICATE_ID( "certificate_id");

    TagColumns(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
