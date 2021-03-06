package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * {@link Certificate} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface CertificateService extends EntityService<Certificate> {

    /**
     * Find {@link Certificate} by {@link Tag} method
     *
     * @param tagId {@link Tag} ID to find by
     *
     */
    List<Certificate> findByTag(int tagId) throws ServiceException;

    /**
     * Add tie between {@link Certificate} and {@link Tag} method
     *
     * @param tagId {@link Tag} ID to tie
     * @param certificate {@link Certificate} to tie
     */
    void addCertificateTag(Certificate certificate, int tagId) throws ServiceException, ValidationException;

    /**
     * Add tie between {@link Certificate} and {@link Tag} method
     *
     * @param tag {@link Tag}  to tie
     * @param certificateId {@link Certificate} ID to tie
     */
    void addCertificateTag(int certificateId, Tag tag) throws ServiceException, ValidationException;

    /**
     * Delete tie between {@link Certificate} and {@link Tag} method
     *
     * @param tagId {@link Tag} ID
     * @param certificateId {@link Certificate} ID
     */
    void deleteCertificateTag(int certificateId, int tagId) throws ServiceException;

    /**
     * Find {@link Certificate} objects by parameters method
     *
     * @param params finding and sorting parameters
     */
    List<Certificate> find(Map<String, String> params) throws ServiceException;
}
