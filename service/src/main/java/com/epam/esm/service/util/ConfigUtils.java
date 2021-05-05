package com.epam.esm.service.util;

import com.epam.esm.service.constants.CertificateSearchParameters;

/**
 * Converting from string request parameter name to enum_like name class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ConfigUtils {
    private final static String UNDESCORE = "_";
    private final static String DASH = "-";

    /**
     * Convert {@link String} line from request parameter format to enum name format.
     *
     * @param parameter Parameter to convert to enum name format.
     * @return {@link CertificateSearchParameters} enum element.
     */
    public static String convertToEnumName(String parameter) {
        return parameter.toUpperCase().replace(DASH, UNDESCORE);
    }

    /**
     * Converts {@link String} line from enum name format to request parameter  format.
     *
     * @param parameter Parameter to convert to request parameter format.
     * @return {@link CertificateSearchParameters} enum element.
     */
    public static String convertToRequestParameter(String parameter) {
        return parameter.toLowerCase().replace(UNDESCORE, DASH);
    }

}
