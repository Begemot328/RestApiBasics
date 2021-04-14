package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
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
    List<Certificate> readByTag(int tagId) throws ServiceException;

    /**
     * Add tie between {@link Certificate} and {@link Tag} method
     *
     * @param tagId {@link Tag} ID to tie
     * @param certificate {@link Certificate} to tie
     */
    void addCertificateTag(Certificate certificate, int tagId) throws ServiceException, BadRequestException;

    /**
     * Add tie between {@link Certificate} and {@link Tag} method
     *
     * @param tag {@link Tag}  to tie
     * @param certificateId {@link Certificate} ID to tie
     */
    void addCertificateTag(int certificateId, Tag tag) throws ServiceException, BadRequestException;

    void addCertificateTags(int certificateId, Tag[] tags) throws ServiceException,
            BadRequestException;

    void addCertificatesTag(Certificate[] certificates, int tagId) throws BadRequestException,
            ServiceException;

    /**
     * Delete tie between {@link Certificate} and {@link Tag} method
     *
     * @param tagId {@link Tag} ID
     * @param certificateId {@link Certificate} ID
     */
    void deleteCertificateTag(int certificateId, int tagId) throws BadRequestException;

    /**
     * Find {@link Certificate} objects by parameters method
     *
     * @param params finding and sorting parameters
     */
    List<Certificate> read(Map<String, String> params) throws ServiceException, BadRequestException;
}
