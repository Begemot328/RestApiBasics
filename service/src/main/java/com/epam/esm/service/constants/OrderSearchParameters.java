package com.epam.esm.service.constants;

import com.epam.esm.service.util.ConfigUtils;

/**
 * Search parameters to search orders.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum OrderSearchParameters {
    USER_ID, ORDER_ID, CERTIFICATE_ID;

    /**
     * Obtain corresponding enum element by {@link String} name.
     *
     * @param parameterName Parameter to find search parameter by.
     * @return {@link CertificateSearchParameters} enum element.
     */
    public static OrderSearchParameters getEntryByParameter(String parameterName) {
        return valueOf(ConfigUtils.convertToEnumName(parameterName));
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
