package com.epam.esm.persistence.util;

public enum FinderQueries {
    WHERE(" WHERE "),
    ORDER_BY(" ORDER BY "),
    LIKE("LIKE '%VALUE%'"),
    VALUE("VALUE");

    FinderQueries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
