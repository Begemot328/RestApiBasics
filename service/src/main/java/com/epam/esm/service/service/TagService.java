package com.epam.esm.service.service;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import java.util.List;
import java.util.Map;

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
     *
     */
    List<Tag> readByCertificate(int certificateId) throws NotFoundException;

    /**
     * Find {@link Tag} objects by parameters method
     *
     * @param params finding and sorting parameters
     */
    List<Tag> read(Map<String, String> params) throws BadRequestException, NotFoundException;
}
