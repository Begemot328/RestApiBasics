package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagServiceImplTests {
    private TagDAOImpl tagDaoMock = Mockito.mock(TagDAOImpl.class);
    private EntityValidator<Tag> validator;
    private TagFinder finder;
    private TagServiceImpl service;

    private Tag tag1 = new Tag("Tag1");
    private Tag tag2 = new Tag("Tag2");
    private Tag tag3 = new Tag("Tag3");
    private Tag tag4 = new Tag("Tag4");
    private Tag[] tags = {tag1, tag2, tag3, tag4};
    private Tag[] tagsShort = {tag1, tag2};
    private List<Tag> fullList = Arrays.asList(tags);
    private List<Tag> shortList = Arrays.asList(tagsShort);

    {
        try {
            Mockito.when(tagDaoMock.readAll()).thenReturn(fullList);
            Mockito.when(tagDaoMock.read(1)).thenReturn(tag1);
            Mockito.when(tagDaoMock.read(2)).thenReturn(tag2);
            Mockito.when(tagDaoMock.readBy(Mockito.any(TagFinder.class))).thenReturn(shortList);
            Mockito.doNothing().when(tagDaoMock).delete(Mockito.any(Integer.class));
            Mockito.doNothing().when(tagDaoMock).update(Mockito.any(Tag.class));
            Mockito.when(tagDaoMock.create(Mockito.any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgumentAt(0, Tag.class);
                tag.setId(fullList.size());
                return tag;
            });

            validator = Mockito.mock(TagValidator.class);
            Mockito.doNothing().when(validator).validate(Mockito.any(Tag.class));
        } catch (DAOSQLException | ValidationException e) {
            e.printStackTrace();
        }
        tag1.setId(1);
        tag2.setId(2);
        tag1.setId(3);
        tag2.setId(4);
        service = new TagServiceImpl(tagDaoMock, validator);
    }

    @Test
    public void readTest() throws ServiceException {
        assertEquals(tag1, service.read(1));
    }

    @Test
    public void findAllTest() throws ServiceException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void createTest() throws ServiceException, ValidationException, DAOSQLException {
        Tag tag = service.create(tag1);
        assertEquals(tag.getId(), fullList.size());
        Mockito.verify(tagDaoMock, Mockito.times(1)).create(tag1);
    }

    @Test
    public void deleteTest() throws ServiceException, DAOSQLException {
        service.delete(1);
        Mockito.verify(tagDaoMock, Mockito.times(1)).read(1);
    }

    @Test
    public void updateTest() throws ServiceException, ValidationException, DAOSQLException {
        service.update(tag2);
        Mockito.verify(tagDaoMock, Mockito.times(1)).update(tag2);
    }

    @Test
    public void findByCertificateTest() throws ServiceException, DAOSQLException {
        assertEquals(shortList, service.readByCertificate(1));
        TagFinder finder = new TagFinder().findByCertificate(1);
        Mockito.verify(tagDaoMock, Mockito.times(1)).readBy(finder);
    }

    @Test
    public void findTest() throws ServiceException, DAOSQLException {
        Map<String, String> params = new HashMap<>();
        params.put(TagSearchParameters.NAME.name(), "1");
        params.put(TagSortingParameters.SORT_BY_NAME.name().toLowerCase(), "2");

        assertEquals(shortList, service.read(params));
        Mockito.verify(tagDaoMock, Mockito.times(1)).readBy(Mockito.any(TagFinder.class));
    }
}
