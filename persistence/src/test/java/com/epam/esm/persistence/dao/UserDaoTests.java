package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.UserColumns;
import com.epam.esm.persistence.dao.user.UserDAOImpl;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
public class UserDaoTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDAOImpl userDao;
    private User user;


    @BeforeEach
    void init() {
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
    void create_createUser() {
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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query = query.select(root);
        query.where(builder.equal(root.get(UserColumns.LOGIN.getValue()), "root"));

        UserFinder finderMock = mock(UserFinder.class);
        when(finderMock.getQuery()).thenReturn(query);

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
    void delete_nonExistingUser_throwDataAccessException() {
        int size = userDao.readAll().size();
        assertThrows(DataAccessException.class, () -> userDao.delete(1000));
        assertEquals(userDao.readAll().size(), size);
    }
}
