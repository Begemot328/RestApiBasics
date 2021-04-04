import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagsDAO extends TestDao {
    private static TagDAOImpl tagsDao;
    Tag tag = new Tag("New tag");
    private static final String TEST_DATABASE = "SQL/test_db.sql";
    private static final String BACKUP_DATABASE = "SQL/db.sql";
    private static JdbcTemplate template;


    private void setDatabase(String database) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(new File(database)));
        scanner.useDelimiter(";");
        while (scanner.hasNext()) {
            template.update(scanner.next().concat(";"));
        }
    }

    @Before
    void init() throws FileNotFoundException {
        setDatabase(BACKUP_DATABASE);
        tagsDao = new TagDAOImpl(new JdbcTemplate(), new TagMapper());
    }

    @After
    public void setBackupDatabase() throws FileNotFoundException {
        setDatabase(BACKUP_DATABASE);
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
