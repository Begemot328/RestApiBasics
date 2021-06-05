package com.epam.esm.service.constants;

import com.epam.esm.service.util.ConfigUtils;
import org.apache.commons.lang3.EnumUtils;

/**
 * Certificate search parameters enum
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum PaginationParameters {
    LIMIT, PAGE;

    /**
     * Obtain enum element by {@link String} name.
     *
     * @param parameter Parameter to find search parameter by.
     * @return {@link PaginationParameters} Corresponding enum element.
     */
    public static PaginationParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertToEnumName(parameter));
    }

    /**
     * Checks if enum contains element with name similar to parameter.
     *
     * @param parameter Parameter to find search parameter by.
     * @return {@link PaginationParameters} Corresponding enum element.
     */
    public static boolean contains(String parameter) {
        return EnumUtils.isValidEnum(PaginationParameters.class, ConfigUtils.convertToEnumName(parameter));
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
