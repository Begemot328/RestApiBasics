import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.AscDesc;
import com.epam.esm.persistence.dao.tag.MySQLTagDAO;
import com.epam.esm.persistence.dao.tag.TagFinder;
import com.epam.esm.persistence.exceptions.DAOException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagsDao extends TestDao {
    private MySQLTagDAO tagsDao = new MySQLTagDAO(getJdbcTemplate());
    Tag tag = new Tag("New tag");

    @Test
    void testFindAll() throws DAOException {
        assertEquals(tagsDao.findAll().size(), 3);
    }

    @Test
    void testFinder() throws DAOException {
        Tag tag = new Tag("Sport game");
        tagsDao.create(tag);
        TagFinder finder = new TagFinder();
        finder.findByName("name");
        assertTrue(tagsDao.findBy(finder).contains(tag));

        Tag cardTag = new Tag("Card game");
        tagsDao.create(cardTag);

        ArrayList<Tag> list = (ArrayList<Tag>) tagsDao.findBy(finder.sortByName(AscDesc.DESC));
        assertTrue(list.indexOf(tag) > list.indexOf(cardTag));
        list = (ArrayList<Tag>) tagsDao.findBy(finder.sortByName(AscDesc.ASC));
        assertTrue(list.indexOf(tag) < list.indexOf(cardTag));

        tagsDao.delete(tag.getId());
    }

    @Test
    void testDao() throws DAOException {
        int size = tagsDao.findAll().size();
        tagsDao.create(tag);
        assertEquals(tagsDao.read(tag.getId()),tag);
        assertEquals(tagsDao.findAll().size(), size + 1);
        assertNotEquals(tag.getId(), 0);

        String newName = "New tag name";
        tag.setName(newName);
        tagsDao.update(tag);
        assertEquals(newName, tagsDao.read(tag.getId()));

        tagsDao.delete(tag.getId());
        assertEquals(tagsDao.findAll().size(), size);
    }


}
