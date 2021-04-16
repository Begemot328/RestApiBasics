package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.TagFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagsDaoTests {
    private static TagDAOImpl tagsDao;

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
    void init() {
        JdbcTemplate template = new JdbcTemplate(dataSource());
        tagsDao = new TagDAOImpl(template, new TagMapper());

    }

    @Test
    void testFindAll() {
        assertEquals(tagsDao.readAll().size(), 5);
    }

    @Test
    void testRead() {
        Tag tag = new Tag("sport");
        tag.setId(1);
        assertEquals(tagsDao.read(1), tag);
    }

    @Test
    void testCreate() throws DAOSQLException {
        Tag tag = new Tag("new");
        int size = tagsDao.readAll().size();

        tagsDao.create(tag);
        assertEquals(tagsDao.readAll().size(), ++size);
    }

    @Test
    void testUpdate() {
        Tag tag = new Tag("new");
        assertThrows(UnsupportedOperationException.class, () -> tagsDao.update(tag));
    }

    @Test
    void testFindBy() {
        TagFinder finderMock = mock(TagFinder.class);
        when(finderMock.getQuery()).thenReturn(" WHERE NAME = 'books'");
        Tag tag = new Tag("books");
        tag.setId(3);
        assertEquals(Collections.singletonList(tag), tagsDao.readBy(finderMock));
    }

    @Test
    void testDelete() {
        int size = tagsDao.readAll().size();

        tagsDao.delete(1);
        assertEquals(tagsDao.readAll().size(), --size);
    }
}
