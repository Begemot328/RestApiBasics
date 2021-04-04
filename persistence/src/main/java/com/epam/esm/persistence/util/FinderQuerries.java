package com.epam.esm.persistence.util;

public enum FinderQuerries {
    WHERE(" WHERE "),
    ORDER_BY(" ORDER BY"),
    LIKE("LIKE '%VALUE%'"),
    VALUE("VALUE");

    FinderQuerries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
