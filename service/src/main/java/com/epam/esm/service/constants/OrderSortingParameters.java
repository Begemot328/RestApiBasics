package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.OrderColumns;
import com.epam.esm.service.util.ConfigUtils;

/**
 * Parameters to sort tags
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum OrderSortingParameters {
    SORT_BY_USER_ID(OrderColumns.USER_ID.getValue()),;

    String value;

    OrderSortingParameters(String value) {
        this.value = value;
    }

    /**
     * Obtain corresponding enum element by {@link String} name.
     *
     * @param parameter Parameter to find order parameter by.
     * @return {@link OrderSortingParameters} enum element.
     */
    public static OrderSortingParameters getEntryByParameter(String parameter) {
        return valueOf(ConfigUtils.convertToEnumName(parameter));
    }

    /**
     * Enum element value getter.
     *
     * @return query String line.
     */
    public String getValue() {
        return value;
    }
}
