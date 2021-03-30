package com.epam.esm.services.service.tag;

import com.epam.esm.persistence.dao.certificate.CertificateColumns;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.dao.tag.TagColumns;

public enum TagSortingParameters {
    SORT_BY_NAME(TagColumns.NAME.getValue());

    String value;

    TagSortingParameters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TagSortingParameters getEntry(String s) {
        try {
            TagSortingParameters.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return TagSortingParameters.valueOf(s);
    }

    public static TagSortingParameters getEntryByParameter(String s) {
        return getEntry(s.toUpperCase().replace("-", "_"));
    }
}
