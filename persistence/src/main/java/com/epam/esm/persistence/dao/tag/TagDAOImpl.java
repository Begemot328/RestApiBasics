package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagQueries;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

/**
 * Tag DAO implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public class TagDAOImpl implements TagDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Default constructor.
     */
    public TagDAOImpl() {
        // Default constructor for Spring purposes.
    }

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag getById(int id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update operation for tag is unavailable");
    }

    @Override
    public void delete(int id) {
        entityManager.remove(getById(id));
    }

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query.orderBy(builder.desc(tagRoot.get("name")));
        query = query.select(tagRoot);
        TypedQuery<Tag> allQuery = entityManager.createQuery(query);
        return allQuery.getResultList();
    }

    @Override
    public CriteriaBuilder getBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }

    @Override
    public List<Tag> findByParameters(EntityFinder<Tag> finder) {
      TypedQuery<Tag> allQuery = getTypedQuery(finder);
        return allQuery.getResultList();
    }

    private TypedQuery<Tag> getTypedQuery(EntityFinder<Tag> finder) {
        TypedQuery<Tag> allQuery = entityManager.createQuery(finder.getQuery());
        allQuery.setFirstResult(finder.getOffset());
        if (finder.getLimit() > 0) {
            allQuery.setMaxResults(finder.getLimit());
        }
        return allQuery;
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
