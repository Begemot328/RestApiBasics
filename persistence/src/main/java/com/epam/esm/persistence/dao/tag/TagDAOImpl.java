package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagQueries;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Tag DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class TagDAOImpl implements TagDAOCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public TagDAOImpl() {
        // Default constructor for Spring purposes.
    }
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public List<Tag> findByQuery(String query) {
        return entityManager.createNativeQuery(query, Tag.class).getResultList();
    }

    @Override
    public Tag findMostPopularTag() {
        List<Tag> tags = findByQuery(TagQueries.SELECT_MOST_POPULAR_TAG.getValue());

        if (CollectionUtils.isEmpty(tags)) {
            return null;
        }
        return tags.get(0);
    }
}
