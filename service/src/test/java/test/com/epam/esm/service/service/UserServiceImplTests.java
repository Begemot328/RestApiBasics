package test.com.epam.esm.service.service;

import com.epam.esm.persistence.dao.UserDAO;
import com.epam.esm.persistence.model.entity.Role;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.user.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    UserService service;
    private User user1;
    private User user2;
    private User user3;

    private List<User> fullList;
    private List<User> shortList;

    @BeforeEach
    void init() {
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
        when(userDaoMock.findAll(any(BooleanExpression.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(shortList));
    }

    @Test
    void getById_returnUser() throws NotFoundException {
        assertEquals(user1, service.getById(1));
    }

    @Test
    void create_createUser() throws BadRequestException, ValidationException {
        when(userDaoMock.findAll(any(BooleanExpression.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.EMPTY_LIST));
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
    void findByParameters_badParameter_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.findByParameters(params, Pageable.unpaged()));
    }

    @Test
    void findByParameters_badParameterValue_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class, () -> service.findByParameters(params, Pageable.unpaged()));
    }
}
