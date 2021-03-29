package com.epam.esm.services.service;

import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.util.AscDesc;

public enum CertificateSortingParameters {
    SORT_BY_CREATE(CertificateDAO.CREATE_DATE),
    SORT_BY_NAME(CertificateDAO.NAME);


    String value;

    CertificateSortingParameters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CertificateSortingParameters getValue(String s) {
        try {
            CertificateSortingParameters.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return CertificateSortingParameters.valueOf(s);
    }
}
