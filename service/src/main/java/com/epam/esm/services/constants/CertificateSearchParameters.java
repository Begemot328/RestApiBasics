package com.epam.esm.services.constants;

public enum CertificateSearchParameters {
    NAME, TAGNAME, DESCRIPTION, PRICE_LESS, PRICE_MORE;

    public static CertificateSearchParameters getEntryByParameter(String s) {
        return valueOf(s.toUpperCase().replace("-", "_"));
    }
}
