package com.epam.esm.service.constants;

public enum CertificateSearchParameters {
    NAME, TAGNAME, DESCRIPTION, PRICE_LESS, PRICE_MORE;

    public static CertificateSearchParameters getEntryByParameter(String parameter) {
        return valueOf(parameter.toUpperCase().replace("-", "_"));
    }
}
