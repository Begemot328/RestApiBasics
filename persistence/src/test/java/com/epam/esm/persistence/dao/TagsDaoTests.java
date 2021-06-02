package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
class TagsDaoTests {
    private Tag tag;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TagDAO tagsDao;

    @BeforeEach
    void init() {
        tag = new Tag("sport");
    }

    @Test
    void findAll_returnAllTags() {
        assertEquals(5,
                IterableUtils.toList(tagsDao.findAll()).size());
    }

    @Test
    void findById_returnTag() {
        tag.setId(1);
        assertEquals(tagsDao.findById(1).get(), tag);
    }

    @Test
    void findById_negativeId_returnNull() {
        assertTrue(tagsDao.findById(-1).isEmpty());
    }

    @Test
    void findById_nonExistingId_returnNull() {
        assertTrue(tagsDao.findById(1000).isEmpty());
    }

    @Test
    void save_createTag() {
        tag = new Tag("new");
        long size = tagsDao.count();

        tagsDao.save(tag);
        assertEquals(tagsDao.count(), ++size);
    }

    @Test
    void save_nullName_throwException() {
        TestTransaction.end();
        tag = new Tag(null);

        assertThrows(DataAccessException.class, () -> tagsDao.save(tag));
    }

    @Test
    void update_updateTagOperation_ExceptionThrown() {
        tag = new Tag("new");
        tagsDao.save(tag);
        assertEquals(tag, tagsDao.findById(tag.getId()).get());
    }

    @Test
    void findByParameters_findByName_returnTags() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query = query.select(tagRoot);
        query.where(builder.equal(tagRoot.get(TagColumns.NAME.getValue()), "books"));

        TagFinder finderMock = mock(TagFinder.class);
        when(finderMock.getQuery()).thenReturn(query);

        tag = new Tag("books");
        tag.setId(3);
        assertEquals(Collections.singletonList(tag), tagsDao.findByParameters(finderMock));
    }

    @Test
    void delete_deleteTag() {
        long size = tagsDao.count();

        tagsDao.delete(tagsDao.findById(1).get());
        assertEquals(tagsDao.count(), --size);
    }

    @Test
    void delete_nonExistingTag_throwDataAccessException() {
        long size = tagsDao.count();

        assertThrows(DataAccessException.class,
                () -> tagsDao.delete(null));
        assertEquals(tagsDao.count(), size);
    }

    @Test
    void readMostlyUsedTag_returnTag() {
        tag = new Tag("games");
        tag.setId(2);
        assertEquals(tag, tagsDao.findMostPopularTag().get());
    }

    @Test
    void readBy_queryString_returnTags() {
        tag = new Tag("bicycle");
        tag.setId(5);
        assertEquals(tagsDao.findByQuery(
                "SELECT DISTINCT id, name FROM certificates.tag_certificates where name = 'bicycle'"),
                Collections.singletonList(tag));
    }
}
