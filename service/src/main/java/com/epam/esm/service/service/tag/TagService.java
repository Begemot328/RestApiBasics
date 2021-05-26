package com.epam.esm.service.service.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.EntityService;

/**
 * {@link Tag} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagService extends EntityService<Tag> {

    /**
     * Find {@link Tag} the most widely used tag of a user with the highest cost of all orders..
     *
     * @return The most widely used {@link Tag} of a user with the highest cost of all orders.
     */
    Tag readMostlyUsedTag() throws NotFoundException;
}
