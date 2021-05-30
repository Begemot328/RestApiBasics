package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.UserColumns;
import com.epam.esm.service.util.ConfigUtils;

/**
 * Parameters to sort tags
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum UserSortingParameters {
    SORT_BY_FIRST_NAME(UserColumns.FIRST_NAME.getValue()),
    SORT_BY_LAST_NAME(UserColumns.LAST_NAME.getValue());

    String value;

    UserSortingParameters(String value) {
        this.value = value;
    }

    /**
     * Obtain enum element by {@link String} name.
     *
     * @param parameter Parameter to find sorting parameter by.
     * @return {@link UserSortingParameters} enum element.
     */
    public static UserSortingParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertToEnumName(parameter));
    }

    /**
     * Value getter
     *
     * @return query String line
     */
    public String getValue() {
        return value;
    }
}
