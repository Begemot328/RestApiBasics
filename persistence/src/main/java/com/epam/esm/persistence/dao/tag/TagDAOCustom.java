package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.Tag;

import java.util.Optional;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagDAOCustom {

    /**
     * Find mostly used by the most contributing user tag.
     *
     * @return {@link Tag} object.
     */
    Optional<Tag> findMostPopularTag();
}
