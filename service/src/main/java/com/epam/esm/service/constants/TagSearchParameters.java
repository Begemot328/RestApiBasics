package com.epam.esm.service.constants;

import com.epam.esm.service.util.ConfigUtils;

public enum TagSearchParameters {
    NAME;

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link CertificateSearchParameters} enum element
     */
    public static TagSearchParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertName(parameter));
    }
}
