package com.epam.esm.service.util;

import com.epam.esm.service.constants.CertificateSearchParameters;

public class ConfigUtils {
    private static String UNDESCORE = "_";
    private static String DASH = "-";

    /**
     * Convert {@link String} line from request parameter format to enum name format
     *
     * @param parameter parameter to convert
     * @return {@link CertificateSearchParameters} enum element
     */
    public static String convertName(String parameter) {
        return parameter.toUpperCase().replace(DASH, UNDESCORE);
    }
}
