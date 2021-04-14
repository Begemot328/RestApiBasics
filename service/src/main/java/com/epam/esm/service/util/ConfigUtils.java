package com.epam.esm.service.util;

import com.epam.esm.service.constants.CertificateSearchParameters;

public class ConfigUtils {
    private final static String UNDESCORE = "_";

    /**
     * Convert {@link String} line from request parameter format to enum name format
     *
     * @param parameter parameter to convert
     * @return {@link CertificateSearchParameters} enum element
     */
    public static String convertName(String parameter) {
        String DASH = "-";
        return parameter.toUpperCase().replace(DASH, UNDESCORE);
    }
}
