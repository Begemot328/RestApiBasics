package com.epam.esm.persistence.util;

import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.model.entity.Certificate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CertificateFinder extends EntityFinder<Certificate> {

    public CertificateFinder findByName(String name) {
        find(CertificateColumns.NAME.getValue() + StringUtils.SPACE
                 + FinderQueries.LIKE.getValue()
                .replace(FinderQueries.VALUE.getValue(),name));
        return this;
    }

    public void findByDescription(String description) {
        find(CertificateColumns.DESCRIPTION.getValue() + " "
                + FinderQueries.LIKE.getValue().replace(
                FinderQueries.VALUE.getValue() ,description));
    }

    public void findByTag(String name) {
        find(CertificateColumns.TAG_NAME.getValue()
                + " = '" + name + "'");
    }

    public void findByPriceLess(BigDecimal price) {
        find(CertificateColumns.PRICE.getValue() + " <= " + price);
    }

    public void findByPriceMore(BigDecimal price) {
        find(CertificateColumns.PRICE.getValue() + " >= " + price);
    }

    public void findByTag(int id) {
        find(CertificateColumns.TAG_ID.getValue() + " = " + id);
    }

    public void findByTagName(String name) {
        find(CertificateColumns.TAG_NAME.getValue() + " = '" + name + "'");
    }
}
