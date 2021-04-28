package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * {@link Tag} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagService extends EntityService<Tag> {

    /**
     * Find {@link Tag} by {@link Certificate} method
     *
     * @param certificateId {@link Certificate} ID to find by
     */
    List<Tag> readByCertificate(int certificateId, int limit, int offset) throws NotFoundException;

    List<Tag> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException;

    /**
     * Find {@link Tag} objects by parameters method
     */
    Tag readMostlyUsedTag() throws NotFoundException;
}
