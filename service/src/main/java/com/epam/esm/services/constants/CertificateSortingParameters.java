package com.epam.esm.services.constants;

import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.persistence.constants.TagColumns;

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

    public static CertificateSortingParameters getEntryByParameter(String s) {
        return valueOf(s.toUpperCase().replace("-", "_"));
    }
}
