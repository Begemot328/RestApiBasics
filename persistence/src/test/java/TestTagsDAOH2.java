import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.TagFinder;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTagsDAOH2 {
    private JdbcTemplate template = new JdbcTemplate(dataSource());
    private TagDAOImpl tagsDao = new TagDAOImpl(template, new TagMapper());
    Tag tag = new Tag("New tag");

    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @Test
    void testFindAll() throws DAOSQLException {
        assertEquals(tagsDao.findAll().size(), 3);
    }

    @Test
    void testFinder() throws DAOSQLException {
        Tag tag = new Tag("Sport game");
        tagsDao.create(tag);
        TagFinder finder = new TagFinder();
        finder.findByName("name");
        assertTrue(tagsDao.findBy(finder).contains(tag));

        Tag cardTag = new Tag("Card game");
        tagsDao.create(cardTag);

        finder = new TagFinder();
        finder.sortBy(TagColumns.NAME.getValue(), AscDesc.DESC);
        ArrayList<Tag> list = (ArrayList<Tag>) tagsDao.findBy(finder);
        assertTrue(list.indexOf(tag) > list.indexOf(cardTag));


        finder = new TagFinder();
        finder.sortBy(TagColumns.NAME.getValue(), AscDesc.ASC);
        list = (ArrayList<Tag>) tagsDao.findBy(finder);
        assertTrue(list.indexOf(tag) < list.indexOf(cardTag));

        tagsDao.delete(tag.getId());
    }

    @Test
    void testDao() throws DAOSQLException {
        int size = tagsDao.findAll().size();
        tagsDao.create(tag);
        assertEquals(tagsDao.read(tag.getId()),tag);
        assertEquals(tagsDao.findAll().size(), size + 1);
        assertNotEquals(tag.getId(), 0);

        String newName = "New tag name";
        tag.setName(newName);
        tagsDao.update(tag);
        assertEquals(newName, tagsDao.read(tag.getId()).getName());

        tagsDao.delete(tag.getId());
        assertEquals(tagsDao.findAll().size(), size);
    }
}
