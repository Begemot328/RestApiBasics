package com.epam.esm.persistence.util;

/**
 * Ascending\descending mode constants
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum SortDirection {
    ASC, DESC;

    private final static String DESC_DIRECTION = "2";

    public static SortDirection parseAscDesc(String param) {
        switch (param) {
            case DESC_DIRECTION:
            case "desc":
                return SortDirection.DESC;
            default:
                return SortDirection.ASC;
        }
    }
}
