package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.QCertificate;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link Certificate} objects
 * via {@link CertificateDAO} datasources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */

@Component
@Scope("prototype")
public class CertificateFinder extends EntityFinder<Certificate> {

    /**
     * Constructor.
     **/
    public CertificateFinder() {
        super();
    }


    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will include.
     */
    public void findByNameLike(String name) {
        add(QCertificate.certificate.name.containsIgnoreCase(name));
    }

    /**
     * Find by description condition adding method.
     *
     * @param description String that found descriptions will include.
     */
    public void findByDescriptionLike(String description) {
        add(QCertificate.certificate.description.containsIgnoreCase(description));
     }

    /**
     * Find by tag name condition adding method
     *
     * @param name String that found tag names will include.
     */
    public void findByTagName(String name) {

        add(QCertificate.certificate.tags.any().name.eq(name));
    }

    /**
     * Find by tag id condition adding method.
     *
     * @param id ID of the tag to find by.
     */
    public void findByTagId(int id) {
        add(QCertificate.certificate.tags.any().id.eq(id));
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
            findByTagName(tag);
        }
    }
}
