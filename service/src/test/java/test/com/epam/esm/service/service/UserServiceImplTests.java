package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
class UserServiceImplTests {

    @MockBean
    UserDAO userDaoMock;
    @Autowired
    UserServiceImpl service;
    @PersistenceContext
    private EntityManager entityManager;
    private User user1;
    private User user2;
    private User user3;

    private List<User> fullList;
    private List<User> shortList;

    @BeforeEach
    void init() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query = query.select(tagRoot);

        user1 = new User("Yury", "Zmushko", "root",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user1.setId(1);
        user1.addRole(new Role("ADMIN"));
        user2 = new User("Yury", "Zmushko", "root",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user2.setId(2);
        user2.addRole(new Role("USER"));
        user3 = new User("Yury", "Zmushko", "root",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user3.setId(3);

        User[] users = {user1, user2, user3};
        User[] usersShort = {user1, user2};
        fullList = Arrays.asList(users);
        shortList = Arrays.asList(usersShort);

        when(userDaoMock.findAll()).thenReturn(fullList);
        when(userDaoMock.findById(1)).thenReturn(Optional.ofNullable(user1));
        when(userDaoMock.findById(2)).thenReturn(Optional.ofNullable(user2));
        when(userDaoMock.findByParameters(any(UserFinder.class))).thenReturn(shortList);
        when(userDaoMock.getBuilder()).thenReturn(builder);
    }

    @Test
    void read_returnUser() {
        assertEquals(user1, service.getById(1).get());
    }

    @Test
    void readAll_returnUsers() throws NotFoundException {
        assertEquals(fullList, service.findAll());
    }

    @Test
    void create_createUser() throws BadRequestException, ValidationException {
        when(userDaoMock.findByParameters(any(UserFinder.class))).thenReturn(Collections.EMPTY_LIST);
        service.create(user2);
        verify(userDaoMock, atLeast(1)).save(user2);
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
        service.findByParameters(params);

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
        service.findByParameters(params);

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
        assertThrows(BadRequestException.class, () -> service.findByParameters(params));
    }

    @Test
    void read_badParameterValue_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder(userDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class, () -> service.findByParameters(params));
    }
}
