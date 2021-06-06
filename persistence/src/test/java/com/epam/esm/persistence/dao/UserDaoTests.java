package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.QUser;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.user.UserDAO;
import com.epam.esm.persistence.util.finder.impl.UserFinder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
class UserDaoTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDAO userDao;
    private User user;

    @BeforeEach
    void init() {
        user = new User("Yury", "Zmushko", "root", 
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user.setId(1);
        Role role = new Role("ADMIN");
        role.setId(1);
        role.setDescription("Super admin");
        user.addRole(role);
    }

    @Test
    void findAll_returnAllUsers() {
        assertEquals(4, IterableUtils.toList(userDao.findAll()).size());
    }

    @Test
    void find_returnUser() {
        assertEquals(userDao.findById(1).get(), user);
    }

    @Test
    void findById_negativeId_returnNull() {
        assertTrue(userDao.findById(-1).isEmpty());
    }

    @Test
    void findById_nonExistingId_returnNull() {
        assertTrue(userDao.findById(-1).isEmpty());
    }

    @Test
    @Transactional
    void save_createUser() {
        User user = new User("Yury2", "Zmushko2", "root2", "qwerty2");
        long size = userDao.count();

        userDao.save(user);
        assertEquals(userDao.count(), ++size);
    }

    @Test
    void save_nullFirstName_throwException() {
        TestTransaction.end();
        user.setFirstName(null);

        assertThrows(DataAccessException.class, () -> userDao.save(user));
    }

    @Test
    void save_nullLastName_throwException() {
        TestTransaction.end();
        user.setLastName(null);

        assertThrows(DataAccessException.class, () -> userDao.save(user));
    }

    @Test
    void save_nullLoginName_throwException() {
        TestTransaction.end();
        user.setLogin(null);

        assertThrows(DataAccessException.class, () -> userDao.save(user));
    }

    @Test
    void save_nullPasswordName_throwException() {
        TestTransaction.end();
        user.setPassword(null);

        assertThrows(DataAccessException.class, () -> userDao.save(user));
    }


    @Test
    void update_updateUserOperation_updateUser() {
        user.setFirstName("new FirstName");
        user.setId(1);
        userDao.save(user);
        assertEquals(user, userDao.findById(user.getId()).get());
    }

    @Test
    void findByParameters_findByName_returnUsers() {
        UserFinder finderMock = mock(UserFinder.class);
        when(finderMock.getPredicate()).thenReturn(QUser.user.login.eq("root"));

        user.setId(1);
        assertEquals(Collections.singletonList(user), userDao.findAll(finderMock.getPredicate()));
    }

    @Test
    void delete_deleteUser() {
        long size = userDao.count();

        userDao.delete(userDao.findById(1).get());
        assertEquals(userDao.count(), --size);
    }

    @Test
    void delete_nonExistingUser_throwDataAccessException() {
        long size = userDao.count();
        assertThrows(DataAccessException.class, () -> userDao.delete(null));
        assertEquals(userDao.count(), size);
    } 
}
