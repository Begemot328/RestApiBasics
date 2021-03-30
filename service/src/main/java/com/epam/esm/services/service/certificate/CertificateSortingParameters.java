package com.epam.esm.services.service.certificate;

import com.epam.esm.persistence.dao.certificate.CertificateColumns;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;

public enum CertificateSortingParameters {
    SORT_BY_NAME(CertificateColumns.NAME.getValue()),
    DESCRIPTION(CertificateColumns.DESCRIPTION.getValue());

    String value;

    CertificateSortingParameters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CertificateSortingParameters getEntry(String s) {
        try {
            CertificateSortingParameters.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return CertificateSortingParameters.valueOf(s);
    }

    public static CertificateSortingParameters getEntryByParameter(String s) {
        return getEntry(s.toUpperCase().replace("-", "_"));
    }
}
