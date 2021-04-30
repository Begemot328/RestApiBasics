package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.TagFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagsDaoTests {
    private static TagDAOImpl tagsDao;
    private Tag tag;

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
    void init() {
        JdbcTemplate template = new JdbcTemplate(dataSource());
        tagsDao = new TagDAOImpl(template, new TagMapper());
        tag = new Tag("sport");

    }

    @Test
    void readAll_returnAllTags() {
        assertEquals(tagsDao.readAll().size(), 5);
    }

    @Test
    void read_returnTag() {
        tag.setId(1);
        assertEquals(tagsDao.read(1), tag);
    }

    @Test
    void read_negativeId_returnNull() {
        assertNull(tagsDao.read(-1));
    }

    @Test
    void read_nonExistingId_returnNull() {
        assertNull(tagsDao.read(1000));
    }

    @Test
    void create_createTag() throws DAOSQLException {
        tag = new Tag("new");
        int size = tagsDao.readAll().size();

        tagsDao.create(tag);
        assertEquals(tagsDao.readAll().size(), ++size);
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
        TagFinder finderMock = mock(TagFinder.class);
        when(finderMock.getQuery()).thenReturn(" WHERE NAME = 'books'");
        tag = new Tag("books");
        tag.setId(3);
        assertEquals(Collections.singletonList(tag), tagsDao.readBy(finderMock));
    }

    @Test
    void delete_deleteTag() {
        int size = tagsDao.readAll().size();

        tagsDao.delete(1);
        assertEquals(tagsDao.readAll().size(), --size);
    }

    @Test
    void delete_nonExistingTag_doNothing() {
        int size = tagsDao.readAll().size();

        tagsDao.delete(1000);
        assertEquals(tagsDao.readAll().size(), size);
    }

    @Test
    void readMostlyUsedTag_returnTag() {
        tag = new Tag("games");
        tag.setId(2);
        assertEquals(tagsDao.readMostlyUsedTag(), tag);
    }

    @Test
    void readBy_queryString_returnTags() {
        tag = new Tag("bicycle");
        tag.setId(5);
        assertEquals(tagsDao.readBy(
                "SELECT DISTINCT id, name FROM certificates.tag_certificates where name = 'bicycle'"),
                Collections.singletonList(tag));
    }

}
