package com.epam.esm.service.constants;

import com.epam.esm.service.util.ParametersUtil;

/**
 * Certificate search parameters enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum CertificateSearchParameters {
    NAME, TAGNAME, DESCRIPTION, PRICE_LESS, PRICE_MORE;

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static CertificateSearchParameters getEntryByParameter(String parameter) {
        return valueOf(ParametersUtil.convertName(parameter));
    }
}
