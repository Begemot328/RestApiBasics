package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.tag.TagServiceImpl;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
public class TagServiceImplTests {

    @PersistenceContext
    private EntityManager entityManager;

    static Logger logger = LoggerFactory.getLogger(CertificateServiceImplTests.class);

    private final TagDAOImpl tagDaoMock = mock(TagDAOImpl.class);
    private EntityValidator<Tag> validator;
    private TagServiceImpl service;

    private final Tag tag1 = new Tag("Tag1");
    private final Tag tag2 = new Tag("Tag2");
    private final Tag tag3 = new Tag("Tag3");
    private final Tag tag4 = new Tag("Tag4");
    private final Tag[] tags = {tag1, tag2, tag3, tag4};
    private final Tag[] tagsShort = {tag1, tag2};
    private final List<Tag> fullList = Arrays.asList(tags);
    private final List<Tag> shortList = Arrays.asList(tagsShort);

    @BeforeEach
    void init() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query = query.select(tagRoot);

        try {
            when(tagDaoMock.readAll()).thenReturn(fullList);
            when(tagDaoMock.read(1)).thenReturn(tag1);
            when(tagDaoMock.read(2)).thenReturn(tag2);
            when(tagDaoMock.readBy(any(TagFinder.class))).thenReturn(shortList);
            doNothing().when(tagDaoMock).delete(any(Integer.class));
            when(tagDaoMock.getBuilder()).thenReturn(builder);
            when(tagDaoMock.create(any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgument(0, Tag.class);
                tag.setId(fullList.size());
                return tag;
            });
            validator = mock(TagValidator.class);
            doNothing().when(validator).validate(any(Tag.class));

            when(tagDaoMock.readMostlyUsedTag()).thenReturn(tag4);

        } catch (ValidationException e) {
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
    public void create_createTag() throws ValidationException, BadRequestException {
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
        assertThrows(ValidationException.class, () -> service.create(tag1));
    }

    @Test
    public void create_catchDataAccessException_throwsException() {

        doThrow(new DataIntegrityViolationException("error"))
                .when(tagDaoMock).create(any(Tag.class));

        assertThrows(BadRequestException.class, () -> service.create(tag1));
    }

    @Test
    public void readMostlyUsedTag_returnTag() throws NotFoundException {
        assertEquals(service.readMostlyUsedTag(), tag4);
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderLimit() throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder(tagDaoMock);
        finder.limit(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(tagDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderOffset() throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder(tagDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(tagDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_badParameter_ThrowsBadRequestException() {
        TagFinder finder = new TagFinder(tagDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }


}
