package com.epam.esm.persistence.util;

/**
 * Ascending\descending mode constants
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum SortDirection {
    ASC, DESC;

    public static SortDirection parseAscDesc(String param) {
        switch (param) {
            case "2":
            case "desc":
                return SortDirection.DESC;
            default:
                return SortDirection.ASC;
        }
    }
}
