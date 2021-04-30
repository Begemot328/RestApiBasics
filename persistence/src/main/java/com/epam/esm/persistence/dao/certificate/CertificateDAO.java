package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

/**
 * Certificate DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface CertificateDAO extends EntityDAO<Certificate> {
    /**
     * Find using {@link EntityFinder} method
     *
     * @param finder {@link EntityFinder} to obtain search parameters
     * @return {@link java.util.ArrayList}  of {@link Tag} objects
     */
    List<Certificate> readBy(EntityFinder<Certificate> finder);

    /**
     * Add connection between  {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId         {@link Tag} id
     */
    void addCertificateTag(int certificateId, int tagId);

    /**
     * Remove connection between {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId         {@link Tag} id
     */
    void deleteCertificateTag(int certificateId, int tagId);

    /**
     * Check connection between {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId         {@link Tag} id
     */
    boolean isTagCertificateTied(int certificateId, int tagId);

    List<Certificate> readBy(String query);
}
