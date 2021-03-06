package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOSQLException;
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
     * @throws DAOSQLException when {@link java.sql.SQLException spotted}
     * @return {@link java.util.ArrayList}  of {@link Tag} objects
     */
    List<Certificate> findBy(EntityFinder<Certificate> finder) throws DAOSQLException;

    /**
     * Add connection between  {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId {@link Tag} id
     */
    void addCertificateTag(int certificateId, int tagId);

    /**
     * Remove connection between {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId {@link Tag} id
     */
    void deleteCertificateTag(int certificateId, int tagId);

    /**
     * Check connection between {@link Tag} and {@link Certificate} method
     *
     * @param certificateId {@link Certificate} id
     * @param tagId {@link Tag} id
     */
    boolean isTagCertificateTied(int certificateId, int tagId);
}
