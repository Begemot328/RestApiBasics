package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
class TagServiceImplTests {

    @PersistenceContext
    private EntityManager entityManager;

    static Logger logger = LoggerFactory.getLogger(TagServiceImplTests.class);

    @MockBean
    TagDAO tagDaoMock;

    @MockBean
    EntityValidator<Tag> validator;

    @Autowired
    TagServiceImpl service;

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
            when(tagDaoMock.findAll()).thenReturn(fullList);
            when(tagDaoMock.findById(1)).thenReturn(Optional.of(tag1));
            when(tagDaoMock.findById(2)).thenReturn(Optional.of(tag2));
            when(tagDaoMock.findByParameters(any(TagFinder.class))).thenReturn(shortList);
            doNothing().when(tagDaoMock).delete(any(Tag.class));
            when(tagDaoMock.getBuilder()).thenReturn(builder);
            when(tagDaoMock.save(any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgument(0, Tag.class);
                tag.setId(fullList.size());
                return tag;
            });

            doNothing().when(validator).validate(any(Tag.class));

            when(tagDaoMock.findMostPopularTag()).thenReturn(Optional.of(tag4));

        } catch (ValidationException e) {
            logger.error(e.getMessage());
        }
        tag1.setId(1);
        tag2.setId(2);
        tag1.setId(3);
        tag2.setId(4);
    }

    @Test
    void read_returnTag() {
        assertEquals(Optional.of(tag1), service.getById(1));
    }

    @Test
    void readAll_returnTags() throws NotFoundException {
        assertEquals(fullList, service.findAll());
    }

    @Test
    void save_throwException() {
        tag1.setId(0);
        assertThrows(BadRequestException.class, () -> service.create(tag1));

    }

    @Test
    void save_createTag() throws ValidationException, BadRequestException {
        tag1.setId(0);
        when(tagDaoMock.findByParameters(any(TagFinder.class))).thenReturn(Collections.EMPTY_LIST);
        Tag tag = service.create(tag1);
        assertEquals(tag.getId(), fullList.size());
        verify(tagDaoMock, times(1)).save(tag1);


    }

    @Test
    void delete_deleteTag() throws BadRequestException {
        service.delete(1);
        verify(tagDaoMock, atLeast(1)).findById(1);
        verify(tagDaoMock, atLeast(1)).delete(tag1);
    }

    @Test
    void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(tag2));
    }

    @Test
    void read_readByName_returnTags() throws BadRequestException, NotFoundException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(TagSearchParameters.NAME.name(), Collections.singletonList("1"));
        params.put(TagSortingParameters.SORT_BY_NAME.name().toLowerCase(), Collections.singletonList("2"));
        assertEquals(shortList, service.findByParameters(params));
        verify(tagDaoMock, atLeast(1)).findByParameters(any(TagFinder.class));
    }

    @Test
    void save_invalidTag_throwsException()
            throws ValidationException {
        doThrow(new ValidationException("error", ErrorCodes.TAG_VALIDATION_EXCEPTION))
                .when(validator).validate(any(Tag.class));
        assertThrows(ValidationException.class, () -> service.create(tag1));
    }

    @Test
    void create_catchDataAccessException_throwsException() {

        doThrow(new DataIntegrityViolationException("error"))
                .when(tagDaoMock).save(any(Tag.class));

        assertThrows(BadRequestException.class, () -> service.create(tag1));
    }

    @Test
    void readMostlyUsedTag_returnTag() throws NotFoundException {
        assertEquals(service.findMostPopularTag(), tag4);
    }

    @Test
    void read_parsePaginationLimit_invokeFinderLimit() throws NotFoundException, BadRequestException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.findByParameters(params);

        ArgumentCaptor<EntityFinder<Tag>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(tagDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getLimit());
    }

    @Test
    void read_parsePaginationLimit_invokeFinderOffset() throws NotFoundException, BadRequestException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.findByParameters(params);

        ArgumentCaptor<EntityFinder<Tag>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(tagDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getOffset());
    }

    @Test
    void read_badParameter_ThrowsBadRequestException() {
        TagFinder finder = new TagFinder(tagDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.findByParameters(params));
    }
}
