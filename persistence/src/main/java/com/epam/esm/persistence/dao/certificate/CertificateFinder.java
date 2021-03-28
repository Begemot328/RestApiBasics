package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.persistence.dao.AscDesc;
import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;

import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.Stream;

public class CertificateFinder extends EntityFinder<Certificate> {

    public CertificateFinder findByName(String name) {
        this.searchCondition = MySQLCertificateDAO.NAME + BLANK + LIKE.replace(VALUE,name);
        return this;
    }

    public CertificateFinder findByTags(Tag[] tags) {
        StringBuilder tagString = new StringBuilder();
        Stream.of(tags).forEach((tag) ->
                tagString.append(tag.getId()).append(DELIMETER).append(BLANK));
        this.searchCondition = MySQLCertificateDAO.TAG_ID + BLANK +
                IN.replace(VALUE,tagString.substring(0, tagString.length() - 3));
        return this;
    }

    public CertificateFinder findByTag(Tag tag) {
        this.searchCondition = MySQLCertificateDAO.TAG_ID + EQUALS + tag.getId();
        return this;
    }

    public CertificateFinder findByDescription(String description) {
        this.searchCondition = MySQLCertificateDAO.DESCRIPTION + BLANK
                + LIKE.replace(VALUE ,description);
        return this;
    }

    public CertificateFinder findByCreateDateEarlier(LocalDate date) {
        this.searchCondition = MySQLCertificateDAO.CREATE_DATE + BLANK
                + LESS_OE + BLANK + Date.valueOf(date);
        return this;
    }

    public CertificateFinder findByCreateDateLater(LocalDate date) {
        this.searchCondition = MySQLCertificateDAO.CREATE_DATE + BLANK
                + MORE_OE + BLANK + Date.valueOf(date);
        return this;
    }

    public CertificateFinder findByLastUpdateDateEarlier(LocalDate date) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE + BLANK
                + LESS_OE + BLANK + Date.valueOf(date);
        return this;
    }

    public CertificateFinder findByLastUpdateDateLater(LocalDate date) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE
                + BLANK + MORE_OE + BLANK + Date.valueOf(date);
        return this;
    }

    public CertificateFinder findByPriceLess(Double price) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE
                + BLANK + LESS_OE + BLANK + price;
        return this;
    }

    public CertificateFinder findByPriceMore(Double price) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE + BLANK
                + MORE_OE + BLANK + price;
        return this;
    }

    public CertificateFinder findByDurationLess(Integer duration) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE + BLANK
                + LESS_OE + BLANK + duration;
        return this;
    }

    public CertificateFinder findByDurationMore(Integer duration) {
        this.searchCondition = MySQLCertificateDAO.LAST_UPDATE_DATE + BLANK
                + MORE_OE + BLANK + duration;
        return this;
    }

    public CertificateFinder sortByCreateDate(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.CREATE_DATE, ascDesc);
    }

    public CertificateFinder sortByLastUpdateDate(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.LAST_UPDATE_DATE, ascDesc);
    }



    public CertificateFinder sortByName(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.NAME, ascDesc);
    }

    public CertificateFinder sortByPrice(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.PRICE, ascDesc);
    }

    public CertificateFinder sortByDuration(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.DURATION, ascDesc);
    }

    public CertificateFinder sortByDescription(AscDesc ascDesc) {
        return (CertificateFinder) sortBy(MySQLCertificateDAO.DESCRIPTION, ascDesc);
    }
}
