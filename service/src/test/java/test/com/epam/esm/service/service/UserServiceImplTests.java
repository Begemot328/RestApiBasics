package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAOImpl;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
class UserServiceImplTests {

    @PersistenceContext
    private EntityManager entityManager;

    @MockBean
    UserDAOImpl userDaoMock;

    @Autowired
    UserServiceImpl service;

    private User user1;
    private User user2;
    private User user3;

    private final User[] users = {user1, user2, user3};
    private final User[] usersShort = {user1, user2};
    private final List<User> fullList = Arrays.asList(users);
    private final List<User> shortList = Arrays.asList(usersShort);

    @BeforeEach
    void init() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query = query.select(tagRoot);

        user1 = new User("Yury", "Zmushko", "root", "qwerty");
        user1.setId(1);
        user2 = new User("Yury", "Zmushko", "root", "qwerty");
        user2.setId(2);
        user3 = new User("Yury", "Zmushko", "root", "qwerty");
        user3.setId(3);

        when(userDaoMock.findAll()).thenReturn(fullList);
        when(userDaoMock.getById(1)).thenReturn(user1);
        when(userDaoMock.getById(2)).thenReturn(user2);
        when(userDaoMock.findByParameters(any(UserFinder.class))).thenReturn(shortList);
        when(userDaoMock.getBuilder()).thenReturn(builder);
    }

    @Test
    void read_returnUser() throws NotFoundException {
        assertEquals(user1, service.read(1));
    }

    @Test
    void readAll_returnUsers() throws NotFoundException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    void create_createUser() {
        assertThrows(UnsupportedOperationException.class, () -> service.create(user2));
    }

    @Test
    void delete_deleteUser() {
        assertThrows(UnsupportedOperationException.class, () -> service.delete(1));
    }

    @Test
    void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(user2));
    }

    @Test
    void read_parsePaginationLimit_invokeFinderLimit()
            throws NotFoundException, BadRequestException {
        UserFinder finder = new UserFinder(userDaoMock);
        finder.limit(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.read(params);

        ArgumentCaptor<EntityFinder<User>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(userDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getOffset());
    }

    @Test
    void read_parsePaginationLimit_invokeFinderOffset()
            throws NotFoundException, BadRequestException {
        UserFinder finder = new UserFinder(userDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.read(params);

        ArgumentCaptor<EntityFinder<User>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(userDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getLimit());
    }

    @Test
    void read_badParameter_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder(userDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }

    @Test
    void read_badParameterValue_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder(userDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }
}
