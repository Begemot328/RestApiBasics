package com.epam.esm.service.service.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.EntityService;

import java.util.List;
import java.util.Optional;

/**
 * {@link Tag} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagService extends EntityService<Tag> {

    /**
     * Find {@link Tag} the most widely used tag of a user with the highest cost of all orders.
     *
     * @return The most widely used {@link Tag} of a user with the highest cost of all orders.
     */
    Tag findMostPopularTag() throws NotFoundException;

    /**
     * Find {@link Tag} by his unique name.
     *
     * @param name Name to find.
     * @return {@link Optional} of {@link Tag} with defined name.
     */
    Optional<Tag> getByName(String name);

    public void makeAllTagsDetached(List<Tag> tags)
            throws BadRequestException, ValidationException;
}
