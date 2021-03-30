package com.epam.esm.services.service.certificate;

public enum CertificateSearchParameters {
    NAME, TAGNAME, DESCRIPTION, PRICE_LESS, PRICE_MORE;

    public static CertificateSearchParameters getEntry(String s) {
        try {
            CertificateSearchParameters.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return CertificateSearchParameters.valueOf(s);
    }

    public static CertificateSearchParameters getEntryByParameter(String s) {
        return getEntry(s.toUpperCase().replace("-", "_"));
    }
}
