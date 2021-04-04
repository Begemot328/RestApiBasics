package com.epam.esm.services.constants;

import com.epam.esm.persistence.constants.TagColumns;

public enum TagSortingParameters {
    SORT_BY_ID(TagColumns.ID.getValue()),
    SORT_BY_NAME(TagColumns.NAME.getValue());

    String value;

    TagSortingParameters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TagSortingParameters getEntryByParameter(String s) {
        return valueOf(s.toUpperCase().replace("-", "_"));
    }
}
