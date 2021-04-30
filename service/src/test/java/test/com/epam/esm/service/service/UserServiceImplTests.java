package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAOImpl;
import com.epam.esm.persistence.util.UserFinder;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {
    private UserDAOImpl userDaoMock = mock(UserDAOImpl.class);
    private UserServiceImpl service;
    private User user1;
    private User user2;
    private User user3;

    private final User[] users = {user1, user2, user3};
    private final User[] usersShort = {user1, user2};
    private final List<User> fullList = Arrays.asList(users);
    private final List<User> shortList = Arrays.asList(usersShort);

    @BeforeEach
    void init() {

        user1 = new User("Yury", "Zmushko", "root", "qwerty");
        user1.setId(1);
        user2 = new User("Yury", "Zmushko", "root", "qwerty");
        user2.setId(2);
        user3 = new User("Yury", "Zmushko", "root", "qwerty");
        user3.setId(3);

        when(userDaoMock.readAll()).thenReturn(fullList);
        when(userDaoMock.read(1)).thenReturn(user1);
        when(userDaoMock.read(2)).thenReturn(user2);
        when(userDaoMock.readBy(any(UserFinder.class))).thenReturn(shortList);

        service = new UserServiceImpl(userDaoMock, null);
    }

    @Test
    public void read_returnUser() throws NotFoundException {
        assertEquals(user1, service.read(1));
    }

    @Test
    public void readAll_returnUsers() throws NotFoundException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void create_createUser() {
        assertThrows(UnsupportedOperationException.class, () -> service.create(user2));
    }

    @Test
    public void delete_deleteUser() {
        assertThrows(UnsupportedOperationException.class, () -> service.delete(1));
    }

    @Test
    public void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(user2));
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderLimit()
            throws NotFoundException, BadRequestException {
        UserFinder finder = new UserFinder();
        finder.limit(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(userDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_parsePaginationLimit_invokeFinderOffset() throws NotFoundException, BadRequestException {
        UserFinder finder = new UserFinder();
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.read(params);
        verify(userDaoMock, atLeast(1)).readBy(finder);
    }

    @Test
    public void read_badParameter_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder();
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }

    @Test
    public void read_badParameterValue_ThrowsBadRequestException() {
        UserFinder finder = new UserFinder();
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }
}
