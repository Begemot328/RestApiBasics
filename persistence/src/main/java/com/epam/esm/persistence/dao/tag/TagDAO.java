package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;

import java.util.List;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagDAO extends EntityDAO<Tag> {
    /**
     * Find using {@link EntityFinder} method.
     *
     * @param query {@link String} query to obtain entities.
     * @return {@link java.util.ArrayList}  of {@link Tag} objects.
     */
    List<Tag> readBy(String query);

    /**
     * Find mostly used by the most contributing user tag.
     *
     * @return {@link Tag} object.
     */
    Tag readMostlyUsedTag();
}
