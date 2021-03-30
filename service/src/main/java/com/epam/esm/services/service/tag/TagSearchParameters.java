package com.epam.esm.services.service.tag;

public enum TagSearchParameters {
    NAME;

    public static TagSearchParameters getEntry(String s) {
        try {
            TagSearchParameters.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return TagSearchParameters.valueOf(s);
    }

    public static TagSearchParameters getEntryByParameter(String s) {
        return getEntry(s.toUpperCase().replace("-", "_"));
    }
}
