package com.epam.esm.persistence.util;

import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CertificateFinder extends EntityFinder<Certificate> {

    public CertificateFinder findByName(String name) {
        this.searchCondition = CertificateColumns.NAME.getValue() + " "
                 + FinderQuerries.LIKE.getValue()
                .replace(FinderQuerries.VALUE.getValue(),name);
        return this;
    }

    public void findByDescription(String description) {
        this.searchCondition = CertificateColumns.DESCRIPTION.getValue() + " "
                + FinderQuerries.LIKE.getValue().replace(
                FinderQuerries.VALUE.getValue() ,description);
    }

    public void findByTag(String name) {
        this.searchCondition = CertificateColumns.TAG_NAME.getValue()
                + " = '" + name + "'";
    }

    public void findByPriceLess(BigDecimal price) {
        this.searchCondition = CertificateColumns.PRICE.getValue() + " <= " + price;
    }

    public void findByPriceMore(BigDecimal price) {
        this.searchCondition = CertificateColumns.PRICE.getValue() + " >= " + price;
    }

    public void findByTag(int id) {
        this.searchCondition = CertificateColumns.TAG_ID.getValue() + " = " + id;
    }
}
