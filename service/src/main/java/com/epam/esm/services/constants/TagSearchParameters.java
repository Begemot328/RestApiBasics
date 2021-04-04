package com.epam.esm.services.constants;

public enum TagSearchParameters {
    NAME;

    public static TagSearchParameters getEntryByParameter(String s) {
        return valueOf(s.toUpperCase().replace("-", "_"));
    }
}
