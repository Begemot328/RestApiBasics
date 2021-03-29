package com.epam.esm.persistence.util;

import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.Stream;

@Component
public class CertificateFinder extends EntityFinder<Certificate> {


    public CertificateFinder findByName(String name) {
        this.searchCondition = CertificateDAO.NAME + BLANK + LIKE.replace(VALUE,name);
        return this;
    }

    public void findByTags(Tag[] tags) {
        StringBuilder tagString = new StringBuilder();
        Stream.of(tags).forEach((tag) ->
                tagString.append(tag.getId()).append(DELIMETER).append(BLANK));
        this.searchCondition = CertificateDAO.TAG_ID + BLANK +
                IN.replace(VALUE,tagString.substring(0, tagString.length() - 3));
    }

    public void findByTag(int id) {
        this.searchCondition = CertificateDAO.TAG_ID + EQUALS + id;
    }

    public void findByDescription(String description) {
        this.searchCondition = CertificateDAO.DESCRIPTION + BLANK
                + LIKE.replace(VALUE ,description);
    }

    public void findByCreateDateEarlier(LocalDate date) {
        this.searchCondition = CertificateDAO.CREATE_DATE + BLANK
                + LESS_OE + BLANK + Date.valueOf(date);
    }

    public void findByCreateDateLater(LocalDate date) {
        this.searchCondition = CertificateDAO.CREATE_DATE + BLANK
                + MORE_OE + BLANK + Date.valueOf(date);
    }

    public void findByLastUpdateDateEarlier(LocalDate date) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE + BLANK
                + LESS_OE + BLANK + Date.valueOf(date);
    }

    public void findByLastUpdateDateLater(LocalDate date) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE
                + BLANK + MORE_OE + BLANK + Date.valueOf(date);
    }

    public void findByPriceLess(Double price) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE
                + BLANK + LESS_OE + BLANK + price;
    }

    public void findByPriceMore(Double price) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE + BLANK
                + MORE_OE + BLANK + price;
    }

    public void findBy(Double price) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE + BLANK
                + MORE_OE + BLANK + price;
    }

    public void findBy(String par, String value) {
        this.searchCondition = par + BLANK
                + MORE_OE + BLANK + value;
    }

    public void findByDurationLess(Integer duration) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE + BLANK
                + LESS_OE + BLANK + duration;
    }

    public void findByDurationMore(Integer duration) {
        this.searchCondition = CertificateDAO.LAST_UPDATE_DATE + BLANK
                + MORE_OE + BLANK + duration;
    }

    public void sortByCreateDate(AscDesc ascDesc) {
        sortBy(CertificateDAO.CREATE_DATE, ascDesc);
    }

    public void sortByLastUpdateDate(AscDesc ascDesc) {
        sortBy(CertificateDAO.LAST_UPDATE_DATE, ascDesc);
    }

}
