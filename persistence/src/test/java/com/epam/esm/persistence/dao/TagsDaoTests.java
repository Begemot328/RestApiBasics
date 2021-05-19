package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private TagDAOImpl tagsDao;

    @BeforeEach
    void init() {
        tag = new Tag("sport");
    }

    @Test
    void readAll_returnAllTags() {
        assertEquals(5, tagsDao.findAll().size());
    }

    @Test
    void read_returnTag() {
        tag.setId(1);
        assertEquals(tagsDao.getById(1), tag);
    }

    @Test
    void read_negativeId_returnNull() {
        assertNull(tagsDao.getById(-1));
    }

    @Test
    void read_nonExistingId_returnNull() {
        assertNull(tagsDao.getById(1000));
    }

    @Test
    void create_createTag() {
        tag = new Tag("new");
        int size = tagsDao.findAll().size();

        tagsDao.create(tag);
        assertEquals(tagsDao.findAll().size(), ++size);
    }

    @Test
    void create_nullName_throwException() {
        tag = new Tag(null);

        assertThrows(DataAccessException.class, () -> tagsDao.create(tag));
    }

    @Test
    void update_updateTagOperation_ExceptionThrown() {
        tag = new Tag("new");
        assertThrows(UnsupportedOperationException.class, () -> tagsDao.update(tag));
    }

    @Test
    void readBy_readByName_returnTags() {
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
        int size = tagsDao.findAll().size();

        tagsDao.delete(1);
        assertEquals(tagsDao.findAll().size(), --size);
    }

    @Test
    void delete_nonExistingTag_throwDataAccessException() {
        int size = tagsDao.findAll().size();

        assertThrows(DataAccessException.class, () -> tagsDao.delete(1000));
        assertEquals(tagsDao.findAll().size(), size);
    }

    @Test
    void readMostlyUsedTag_returnTag() {
        tag = new Tag("games");
        tag.setId(2);
        assertEquals(tagsDao.findMostPopularTag(), tag);
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
