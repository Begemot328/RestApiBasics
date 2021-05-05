package com.epam.esm.service.constants;

import com.epam.esm.service.util.ConfigUtils;

/**
 * Certificate search parameters enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum CertificateSearchParameters {
    NAME, DESCRIPTION, TAG_NAME, TAG_ID;

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static CertificateSearchParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertToEnumName(parameter));
    }

    /**
     * Returns  request parameter name corresponding to enum element.
     *
     * @return {@link String} Corresponding request parameter.
     */
    public String getParameterName() {
        return ConfigUtils.convertToRequestParameter(name());
    }
}
