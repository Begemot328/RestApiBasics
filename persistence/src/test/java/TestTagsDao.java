import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagColumns;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagsDao extends TestDao {
    private TagDAO tagsDao = new TagDAO(getJdbcTemplate());
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
    void testDao() throws DAOException {
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
