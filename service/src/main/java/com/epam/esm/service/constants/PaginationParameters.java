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
    LIMIT, OFFSET;

    /**
     * Obtain enum element by {@link String} name
     *
     * @param parameter parameter to find search parameter by
     * @return {@link PaginationParameters} enum element
     */
    public static PaginationParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertName(parameter));
    }

    public static boolean contains(String parameter) {
        return EnumUtils.isValidEnum(PaginationParameters.class, ConfigUtils.convertName(parameter));
    }
}
