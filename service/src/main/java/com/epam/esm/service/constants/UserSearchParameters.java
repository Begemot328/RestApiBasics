package com.epam.esm.service.constants;

import com.epam.esm.service.util.ConfigUtils;

/**
 * Search parameters to search users
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum UserSearchParameters {
    FIRST_NAME, LAST_NAME, LOGIN;

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static UserSearchParameters getEntryByParameter(String parameter) {
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
