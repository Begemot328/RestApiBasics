package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.service.util.ConfigUtils;

/**
 * Parameters to sort tags
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum TagSortingParameters {
    SORT_BY_ID(TagColumns.ID.getValue()),
    SORT_BY_NAME(TagColumns.NAME.getValue());

    String value;

    TagSortingParameters(String value) {
        this.value = value;
    }

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static TagSortingParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertToEnumName(parameter));
    }

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }
}
