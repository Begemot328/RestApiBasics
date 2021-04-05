package com.epam.esm.service.constants;

import com.epam.esm.persistence.constants.CertificateColumns;

public enum CertificateSortingParameters {
    SORT_BY_ID(CertificateColumns.ID.getValue()),
    SORT_BY_NAME(CertificateColumns.NAME.getValue()),
    SORT_BY_DESCRIPTION(CertificateColumns.DESCRIPTION.getValue());

    String value;

    CertificateSortingParameters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CertificateSortingParameters getEntryByParameter(String parameter) {
        return valueOf(parameter.toUpperCase().replace("-", "_"));
    }
}
