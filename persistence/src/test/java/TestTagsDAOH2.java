import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.TagFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTagsDAOH2 {
    private static JdbcTemplate template;
    private static TagDAOImpl tagsDao;
    Tag tag = new Tag("New tag");

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeAll
    static void init() {
        template = new JdbcTemplate(dataSource());
        tagsDao = new TagDAOImpl(template, new TagMapper());

    }

    @Test
    void testFindAll() throws DAOSQLException {
        assertEquals(tagsDao.findAll().size(), 5);
    }

    @Test
    void testRead() throws DAOSQLException {
        Tag tag = new Tag("sport");
        tag.setId(1);
        assertEquals(tagsDao.read(1), tag);
    }

    @Test
    void testCreate() throws DAOSQLException {
        Tag tag = new Tag("new");
        int size = tagsDao.findAll().size();

        tagsDao.create(tag);
        assertEquals(tagsDao.findAll().size(), ++size);
    }

    @Test
    void testUpdate() throws DAOSQLException {
        Tag tag = new Tag("new");
        tag.setId(3);

        assertThrows(UnsupportedOperationException.class, () -> tagsDao.update(tag));
    }

    @Test
    void testFindBy() throws DAOSQLException {
        TagFinder finderMock = Mockito.mock(TagFinder.class);
        Mockito.when(finderMock.getQuery()).thenReturn(" WHERE NAME = 'books'");
        Tag tag = new Tag("books");
        tag.setId(3);
        assertEquals(Collections.singletonList(tag), tagsDao.findBy(finderMock));
    }

    @Test
    void testDelete() throws DAOSQLException {
        int size = tagsDao.findAll().size();

        tagsDao.delete(1);
        assertEquals(tagsDao.findAll().size(), --size);
    }
}
