package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.UserMapper;
import com.epam.esm.persistence.util.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoTests {
    private static UserDAOImpl userDao;
    private User user;

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
    void init() {
        JdbcTemplate template = new JdbcTemplate(dataSource());
        userDao = new UserDAOImpl(template, new UserMapper());
        user = new User("Yury", "Zmushko", "root", "qwerty");
        user.setId(1);

    }

    @Test
    void readAll_returnAllUsers() {
        assertEquals(userDao.readAll().size(), 4);
    }

    @Test
    void read_returnUser() {
        assertEquals(userDao.read(1), user);
    }

    @Test
    void read_negativeId_returnNull() {
        assertNull(userDao.read(-1));
    }

    @Test
    void read_nonExistingId_returnNull() {
        assertNull(userDao.read(1000));
    }

    @Test
    @Transactional
    void create_createUser() throws DAOSQLException {
        User user = new User("Yury2", "Zmushko2", "root2", "qwerty2");
        int size = userDao.readAll().size();

        userDao.create(user);
        assertEquals(userDao.readAll().size(), ++size);
    }

    @Test
    void create_nullFirstName_throwException() {
        user.setFirstName(null);

        assertThrows(DataAccessException.class, () -> userDao.create(user));
    }

    @Test
    void create_nullLastName_throwException() {
        user.setLastName(null);

        assertThrows(DataAccessException.class, () -> userDao.create(user));
    }

    @Test
    void create_nullLoginName_throwException() {
        user.setLogin(null);

        assertThrows(DataAccessException.class, () -> userDao.create(user));
    }

    @Test
    void create_nullPasswordName_throwException() {
        user.setPassword(null);

        assertThrows(DataAccessException.class, () -> userDao.create(user));
    }


    @Test
    void update_updateUserOperation_ExceptionThrown() {
        user.setFirstName("new FirstName");
        user.setId(1);
        userDao.update(user);
        assertEquals(user, userDao.read(user.getId()));
    }

    @Test
    void readBy_readByName_returnUsers() {
        UserFinder finderMock = mock(UserFinder.class);

        when(finderMock.getQuery()).thenReturn(" WHERE LOGIN = 'root'");
        user.setId(1);
        assertEquals(Collections.singletonList(user), userDao.readBy(finderMock));
    }

    @Test
    void delete_deleteUser() {
        int size = userDao.readAll().size();

        userDao.delete(1);
        assertEquals(userDao.readAll().size(), --size);
    }

    @Test
    void delete_nonExistingUser_doNothing() {
        int size = userDao.readAll().size();

        userDao.delete(1000);
        assertEquals(userDao.readAll().size(), size);
    }

    @Test
    void getPassword_returnPassword() {
        String login = "root";
        assertEquals("qwerty", userDao.getPassword(login));
    }
}
