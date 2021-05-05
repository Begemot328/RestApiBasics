package com.epam.esm.persistence.util.finder;

/**
 * Ascending\descending mode constants
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public enum SortDirection {
    ASC, DESC;

    private final static String DESC_DIRECTION = "2";

    /**
     * Sort direction parser.
     *
     * @param param String to parse.
     * @return SortDirection enum element.
     */
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
