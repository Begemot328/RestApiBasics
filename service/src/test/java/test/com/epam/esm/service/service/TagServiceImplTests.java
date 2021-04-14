package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagServiceImplTests {
    static Logger logger = LoggerFactory.getLogger(CertificateServiceImplTests.class);

    private TagDAOImpl tagDaoMock = mock(TagDAOImpl.class);
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

    @BeforeEach
    void init() {
        try {
            when(tagDaoMock.readAll()).thenReturn(fullList);
            when(tagDaoMock.read(1)).thenReturn(tag1);
            when(tagDaoMock.read(2)).thenReturn(tag2);
            when(tagDaoMock.readBy(any(TagFinder.class))).thenReturn(shortList);
            doNothing().when(tagDaoMock).delete(any(Integer.class));
            when(tagDaoMock.create(any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgument(0, Tag.class);
                tag.setId(fullList.size());
                return tag;
            });
            validator = mock(TagValidator.class);
            doNothing().when(validator).validate(any(Tag.class));

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
    public void testRead() throws ServiceException {
        assertEquals(tag1, service.read(1));
    }

    @Test
    public void testFindAll() throws ServiceException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void testCreate() throws ServiceException, DAOSQLException {
        Tag tag = service.create(tag1);
        assertEquals(tag.getId(), fullList.size());
        verify(tagDaoMock, times(1)).create(tag1);
    }

    @Test
    public void testDelete() throws ServiceException {
        service.delete(1);
        verify(tagDaoMock, times(1)).read(1);
    }

    @Test
    public void testUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(tag2));
    }

    @Test
    public void testFindByCertificate() throws ServiceException {
        assertEquals(shortList, service.readByCertificate(1));
        TagFinder finder = new TagFinder().findByCertificate(1);
        verify(tagDaoMock, times(1)).readBy(finder);
    }

    @Test
    public void testFind() throws ServiceException, BadRequestException {
        Map<String, String> params = new HashMap<>();
        params.put(TagSearchParameters.NAME.name(), "1");
        params.put(TagSortingParameters.SORT_BY_NAME.name().toLowerCase(), "2");
        assertEquals(shortList, service.read(params));
        verify(tagDaoMock, times(1)).readBy(any(TagFinder.class));
    }


    @Test
    public void testCreateIfInvalidTag()
            throws ValidationException {
        doThrow(new ValidationException("error"))
            .when(validator).validate(any(Tag.class));
        assertThrows(ServiceException.class,() -> service.create(tag1));
    }

    @Test
    public void testCreateIfInvalidDatabase()
            throws DAOSQLException {
        when(tagDaoMock.create(any(Tag.class))).thenThrow(new DAOSQLException("error"));
        assertThrows(ServiceException.class,() -> service.create(tag1));
    }
}
