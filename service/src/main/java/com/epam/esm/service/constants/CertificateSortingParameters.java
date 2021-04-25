package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.service.util.ConfigUtils;

/**
 * Certificate search parameters enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum CertificateSortingParameters {
    SORT_BY_ID(CertificateColumns.ID.getValue()),
    SORT_BY_NAME(CertificateColumns.NAME.getValue()),
    SORT_BY_DESCRIPTION(CertificateColumns.DESCRIPTION.getValue());

    String value;

    /**
     * Value getter
     */
    CertificateSortingParameters(String value) {
        this.value = value;
    }

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static CertificateSortingParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertName(parameter));
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
