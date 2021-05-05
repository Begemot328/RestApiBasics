package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link Certificate} objects
 * via {@link CertificateDAO} datasources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class CertificateFinder extends EntityFinder<Certificate> {

    /**
     * Constructor.
     *
     * @param dao {@link EntityDAO} object to obtain {@link javax.persistence.criteria.CriteriaBuilder}
     *                             and {@link javax.persistence.metamodel.Metamodel objects}
     */
    public CertificateFinder(EntityDAO<Certificate> dao) {
        super(dao);
    }


    @Override
    protected Class<Certificate> getClassType() {
        return Certificate.class;
    }

    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will include.
     */
    public void findByName(String name) {
        add(builder.like(root.get(CertificateColumns.NAME.getValue()), "%" + name + "%"));
    }

    /**
     * Find by description condition adding method.
     *
     * @param description String that found descriptions will include.
     */
    public void findByDescription(String description) {
        add(builder.like(root.get(CertificateColumns.DESCRIPTION.getValue()), "%" + description + "%"));
    }

    /**
     * Find by tag name condition adding method
     *
     * @param name String that found tag names will include.
     */
    public void findByTag(String name) {
        add(builder.equal(root.join("tags").get("name"), name));
    }

    /**
     * Find by tag id condition adding method.
     *
     * @param id ID of the tag to find by.
     */
    public void findByTagId(int id) {
        add(builder.equal(root.join("tags").get("id"), id));

    }

    /**
     * Find by tag IDs condition adding method.
     *
     * @param tags Array of the tag IDs to find by.
     */
    public void findByTags(int[] tags) {
        for (int tagId : tags) {
            findByTagId(tagId);
        }
    }

    /**
     * Find by tag names condition adding method.
     *
     * @param tags Array of the tag names to find by.
     */
    public void findByTags(String[] tags) {
        for (String tag : tags) {
            findByTag(tag);
        }
    }
}
