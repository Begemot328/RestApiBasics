package com.epam.esm.service.constants;

public enum TagSearchParameters {
    NAME;

    public static TagSearchParameters getEntryByParameter(String parameter) {
        return valueOf(parameter.toUpperCase().replace("-", "_"));
    }
}
