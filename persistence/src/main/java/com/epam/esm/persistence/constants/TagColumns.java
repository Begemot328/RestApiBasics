package com.epam.esm.persistence.constants;

public enum TagColumns {
    ID("id"),
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
