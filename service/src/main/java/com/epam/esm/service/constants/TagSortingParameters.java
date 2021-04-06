package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.TagColumns;

public enum TagSortingParameters {
    SORT_BY_ID(TagColumns.ID.getValue()),
    SORT_BY_NAME(TagColumns.NAME.getValue());

    String value;

    TagSortingParameters(String value) {
        this.value = value;
    }

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static TagSortingParameters getEntryByParameter(String parameter) {
        return valueOf(parameter.toUpperCase().replace("-", "_"));
    }
}
