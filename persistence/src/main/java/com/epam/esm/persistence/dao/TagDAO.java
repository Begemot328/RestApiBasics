package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.model.entity.QTag;
import com.epam.esm.persistence.model.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public interface TagDAO extends EntityRepository<Tag, QTag, Integer> {
    String SELECT_MOST_POPULAR_TAG = "SELECT count(tag_id), name, t.id " +
            "FROM certificates.orders LEFT JOIN certificates.certificate_tag ct " +
            "ON orders.certificate_id = ct.certificate_id LEFT JOIN certificates.tag t " +
            "ON t.id = ct.tag_id JOIN (SELECT user_id, amount_sum" +
            " FROM (SELECT user_id, sum(amount) AS amount_sum" +
            " FROM certificates.orders GROUP BY user_id) AS t2" +
            " ORDER BY amount_sum DESC LIMIT 1) AS t1 ON t1.user_id = orders.user_id" +
            " GROUP BY tag_id ORDER BY COUNT(tag_id) DESC LIMIT 1";

    /**
     * Find {@link Tag} object by name.
     *
     * @return {@link Tag} object.
     * @param name Name to find by.
     */
    Optional<Tag> getByName(String name);

    /**
     * Find mostly used by the most contributing user tag.
     *
     * @return {@link Tag} object.
     */
    @Query(value = SELECT_MOST_POPULAR_TAG, nativeQuery = true)
    Optional<Tag> findMostPopularTag();
}
