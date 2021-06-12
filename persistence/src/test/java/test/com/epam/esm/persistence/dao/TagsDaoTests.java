package test.com.epam.esm.persistence.dao;

import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.model.entity.QTag;
import com.epam.esm.persistence.model.entity.Role;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.model.userdetails.Account;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
class TagsDaoTests {
    private Tag tag;

    @Autowired
    private TagDAO tagsDao;

    @MockBean
    private AuditorAware<Account> auditorAware;

    void initAuditorAware() {
        User user = new User("Yury", "Zmushko", "root",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user.setId(1);
        Role role = new Role("ADMIN");
        role.setId(1);
        role.setDescription("Super admin");
        user.addRole(role);
        Account account = new Account(user);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(account));
    }

    @BeforeEach
    void init() {
        tag = new Tag("sport");
        initAuditorAware();
    }

    @Test
    void findAll_returnAllTags() {
        assertEquals(5,
                IterableUtils.toList(tagsDao.findAll()).size());
    }

    @Test
    void findById_returnTag() {
        tag.setId(1);
        assertEquals(tagsDao.findById(1).get(), tag);
    }

    @Test
    void findById_negativeId_returnNull() {
        assertTrue(tagsDao.findById(-1).isEmpty());
    }

    @Test
    void findById_nonExistingId_returnNull() {
        assertTrue(tagsDao.findById(1000).isEmpty());
    }

    @Test
    void save_createTag() {
        tag = new Tag("new");
        long size = tagsDao.count();

        tagsDao.save(tag);
        assertEquals(tagsDao.count(), ++size);
    }

    @Test
    void save_nullName_throwException() {
        TestTransaction.end();
        tag = new Tag(null);

        assertThrows(DataAccessException.class, () -> tagsDao.save(tag));
    }

    @Test
    void update_updateTagOperation_ExceptionThrown() {
        tag = new Tag("new");
        tagsDao.save(tag);
        assertEquals(tag, tagsDao.findById(tag.getId()).get());
    }

    @Test
    void findByParameters_findByName_returnTags() {
        TagFinder finderMock = mock(TagFinder.class);
        when(finderMock.getPredicate()).thenReturn(QTag.tag.name.eq("books"));

        tag = new Tag("books");
        tag.setId(3);
        assertEquals(Collections.singletonList(tag), tagsDao.findAll(finderMock.getPredicate()));
    }

    @Test
    void delete_deleteTag() {
        long size = tagsDao.count();

        tagsDao.delete(tagsDao.findById(1).get());
        assertEquals(tagsDao.count(), --size);
    }

    @Test
    void delete_nonExistingTag_throwDataAccessException() {
        long size = tagsDao.count();

        assertThrows(DataAccessException.class,
                () -> tagsDao.delete(null));
        assertEquals(tagsDao.count(), size);
    }

    @Test
    void readMostlyUsedTag_returnTag() {
        tag = new Tag("games");
        tag.setId(2);
        assertEquals(tag, tagsDao.findMostPopularTag().get());
    }
}