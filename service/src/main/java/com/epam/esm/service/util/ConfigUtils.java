package com.epam.esm.service.util;

import com.epam.esm.service.constants.CertificateSearchParameters;

/**
 * Converting from string request parameter name to enum_like name class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ConfigUtils {
    private static final  String CONSTANT_NAME_DELIMETER = "_";
    private static final  String REQUEST_PARAM_DELIMITER = "-";

    private ConfigUtils() {
        // Private constructor to avoid creation.
    }

    /**
     * Convert {@link String} line from request parameter format to enum name format.
     *
     * @param parameter Parameter to convert to enum name format.
     * @return {@link CertificateSearchParameters} enum element.
     */
    public static String convertToEnumName(String parameter) {
        return parameter.toUpperCase().replace(REQUEST_PARAM_DELIMITER, CONSTANT_NAME_DELIMETER);
    }

    /**
     * Converts {@link String} line from enum name format to request parameter  format.
     *
     * @param parameter Parameter to convert to request parameter format.
     * @return {@link CertificateSearchParameters} enum element.
     */
    public static String convertToRequestParameter(String parameter) {
        return parameter.toLowerCase().replace(CONSTANT_NAME_DELIMETER, REQUEST_PARAM_DELIMITER);
    }
}
