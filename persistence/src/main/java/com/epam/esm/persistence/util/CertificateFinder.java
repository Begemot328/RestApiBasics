package com.epam.esm.persistence.util;

import com.epam.esm.model.entity.Entity;
import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.model.entity.Certificate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Search criteria class to find {@link Certificate} objects
 * via {@link com.epam.esm.persistence.dao.CertificateDAO} datasources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class CertificateFinder extends EntityFinder<Certificate> {

    /**
     * Find by name condition adding method
     *
     * @param name string that found names will include
     * @return search condition
     */
    public CertificateFinder findByName(String name) {
        find(CertificateColumns.NAME.getValue() + StringUtils.SPACE
                 + FinderQueries.LIKE.getValue()
                .replace(FinderQueries.VALUE.getValue(),name));
        return this;
    }

    /**
     * Find by description condition adding method
     *
     * @param description string that found descriptions will include
     * @return search condition
     */
    public void findByDescription(String description) {
        find(CertificateColumns.DESCRIPTION.getValue() + " "
                + FinderQueries.LIKE.getValue().replace(
                FinderQueries.VALUE.getValue() ,description));
    }

    /**
     * Find by tag name condition adding method
     *
     * @param name string that found descriptions will include
     * @return search condition
     */
    public void findByTag(String name) {
        find(CertificateColumns.TAG_NAME.getValue()
                + " = '" + name + "'");
    }

    /**
     * Find by maximal price condition adding method
     *
     * @param price value of maximal price
     * @return search condition
     */
    public void findByPriceLess(BigDecimal price) {
        find(CertificateColumns.PRICE.getValue() + " <= " + price);
    }

    /**
     * Find by minimal price condition adding method
     *
     * @param price value of minimal price
     * @return search condition
     */
    public void findByPriceMore(BigDecimal price) {
        find(CertificateColumns.PRICE.getValue() + " >= " + price);
    }

    /**
     * Find by tag id condition adding method
     *
     * @param id of the tag to find by
     * @return search condition
     */
    public void findByTag(int id) {
        find(CertificateColumns.TAG_ID.getValue() + " = " + id);
    }

}
