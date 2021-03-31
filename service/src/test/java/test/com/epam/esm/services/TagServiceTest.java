package test.com.epam.esm.services;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagColumns;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.tag.TagSearchParameters;
import com.epam.esm.services.service.tag.TagService;
import com.epam.esm.services.service.tag.TagSortingParameters;
import com.epam.esm.services.validator.EntityValidator;
import com.epam.esm.services.validator.TagValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagServiceTest {
    private TagDAO daoMock = Mockito.mock(TagDAO.class);
    private EntityValidator<Tag> validator;
    private TagFinder finder = new TagFinder();
    private TagService service;

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
            Mockito.when(daoMock.findAll()).thenReturn(fullList);

            Mockito.when(daoMock.read(1)).thenReturn(tag1);
            Mockito.when(daoMock.read(2)).thenReturn(tag2);

            Mockito.when(daoMock.findBy(Mockito.any(TagFinder.class))).thenReturn(shortList);
            Mockito.doNothing().when(daoMock).delete(Mockito.any(Integer.class));
            Mockito.doNothing().when(daoMock).update(Mockito.any(Tag.class));
            Mockito.when(daoMock.create(Mockito.any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgumentAt(0, Tag.class);
                tag.setId(fullList.size());
                return tag;
            });

            validator = Mockito.mock(TagValidator.class);
            Mockito.doNothing().when(validator).validate(Mockito.any(Tag.class));
        } catch (DAOException | ValidationException e) {
            e.printStackTrace();
        }
        tag1.setId(1);
        tag2.setId(2);
        tag1.setId(3);
        tag2.setId(4);

        service = new TagService(daoMock, validator, finder);
    }

    @Test
    public void readTest() throws ServiceException {
        assertEquals(tag1, service.read(1));
    }

    @Test
    public void findAllTest() throws ServiceException {
        assertEquals(fullList, service.findAll());
    }

    @Test
    public void createTest() throws ServiceException, ValidationException, DAOException {
        Tag tag = service.create(tag1);
        assertEquals(tag.getId(), fullList.size());
        Mockito.verify(daoMock, Mockito.times(1)).create(tag1);
    }

    @Test
    public void deleteTest() throws ServiceException, DAOException {
        service.delete(1);
        Mockito.verify(daoMock, Mockito.times(1)).read(1);
    }

    @Test
    public void updateTest() throws ServiceException, ValidationException, DAOException {
        service.update(tag2);
        Mockito.verify(daoMock, Mockito.times(1)).update(tag2);
    }

    @Test
    public void findByCertificateTest() throws ServiceException, DAOException {
        assertEquals(shortList, service.findByCertificate(1));
        TagFinder finder = new TagFinder().findByCertificate(1);
        Mockito.verify(daoMock, Mockito.times(1)).findBy(finder);
    }

    @Test
    public void findTest() throws ServiceException, DAOException {
        Map<String, String> params = new HashMap<>();
        params.put(TagSearchParameters.NAME.name(), "1");
        params.put(TagSortingParameters.SORT_BY_NAME.name(), "2");
        TagFinder finder = new TagFinder();
        finder.newFinder();
        finder.findByName("1");
        finder.sortBy(TagColumns.NAME.getValue(), AscDesc.DESC);
        assertEquals(shortList, service.find(params));
        Mockito.verify(daoMock, Mockito.times(1)).findBy(Mockito.eq(finder));
    }
}
