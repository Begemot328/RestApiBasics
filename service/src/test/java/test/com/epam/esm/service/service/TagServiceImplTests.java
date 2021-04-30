package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.tag.TagServiceImpl;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagServiceImplTests {
    static Logger logger = LoggerFactory.getLogger(CertificateServiceImplTests.class);

    private TagDAOImpl tagDaoMock = mock(TagDAOImpl.class);
    private EntityValidator<Tag> validator;
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

            when(tagDaoMock.readMostlyUsedTag()).thenReturn(tag4);

        } catch (DAOSQLException | ValidationException e) {
            logger.error(e.getMessage());
        }
        tag1.setId(1);
        tag2.setId(2);
        tag1.setId(3);
        tag2.setId(4);
        service = new TagServiceImpl(tagDaoMock, validator);
    }

    @Test
    public void read_returnTag() throws NotFoundException {
        assertEquals(tag1, service.read(1));
    }

    @Test
    public void readAll_returnTags() throws NotFoundException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void create_createTag() throws ServiceException, DAOSQLException {
        Tag tag = service.create(tag1);
        assertEquals(tag.getId(), fullList.size());
        verify(tagDaoMock, times(1)).create(tag1);
    }

    @Test
    public void delete_deleteTag() throws BadRequestException {
        service.delete(1);
        verify(tagDaoMock, atLeast(1)).read(1);
        verify(tagDaoMock, atLeast(1)).delete(1);
    }

    @Test
    public void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(tag2));
    }

    @Test
    public void read_readByName_returnTags() throws BadRequestException, NotFoundException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(TagSearchParameters.NAME.name(), Collections.singletonList("1"));
        params.put(TagSortingParameters.SORT_BY_NAME.name().toLowerCase(), Collections.singletonList("2"));
        assertEquals(shortList, service.read(params));
        verify(tagDaoMock, atLeast(1)).readBy(any(TagFinder.class));
    }

    @Test
    public void create_invalidTag_throwsException()
            throws ValidationException {
        doThrow(new ValidationException("error", ErrorCodes.TAG_VALIDATION_EXCEPTION))
                .when(validator).validate(any(Tag.class));
        assertThrows(ServiceException.class, () -> service.create(tag1));
    }

    @Test
    public void create_catchSQLException_throwsException()
            throws DAOSQLException {
        when(tagDaoMock.create(any(Tag.class))).thenThrow(new DAOSQLException("error"));
        assertThrows(ServiceException.class, () -> service.create(tag1));
    }

    @Test
    public void readMostlyUsedTag_returnTag() throws NotFoundException {
        assertEquals(service.readMostlyUsedTag(), tag4);
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderLimit() throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder();
        finder.limit(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(tagDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderOffset() throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder();
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(tagDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_badParameter_ThrowsBadRequestException() {
        TagFinder finder = new TagFinder();
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }
}
