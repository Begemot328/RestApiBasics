package test.com.epam.esm.persistence.dao;

import com.epam.esm.persistence.dao.OrderDAO;
import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.QOrder;
import com.epam.esm.persistence.model.entity.Role;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.model.userdetails.Account;
import com.epam.esm.persistence.util.finder.impl.OrderFinder;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
class OrderDaoTests {

    @Autowired
    private OrderDAO orderDao;

    private Order order;

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
        initAuditorAware();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Certificate certificate = new Certificate("nastolka.by",
                BigDecimal.valueOf(40.1), 50);
        certificate.setDescription("board games certificate");
        certificate.setCreateDate(LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        certificate.setId(2);

        Tag tag1 = new Tag("games");
        tag1.setId(2);
        Tag tag2 = new Tag("bicycle");
        tag2.setId(5);
        certificate.setTags(Arrays.asList(tag1, tag2));

        User user = new User("Ivan", "Ivanov", "Ivanov",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user.setId(2);
        Role role = new Role("USER");
        role.setId(2);
        role.setDescription("Regular user");
        user.addRole(role);
        order = new Order(certificate, user, BigDecimal.valueOf(150.0), 1,
                LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        order.setId(1);
    }

    @Test
    void readAll_returnAllOrders() {
        assertEquals(6, IterableUtils.toList(orderDao.findAll()).size());
    }

    @Test
    void findById_returnOrder() {
        Order persistentOrder = orderDao.findById(1).get();
        assertEquals(persistentOrder, order);
    }

    @Test
    void findById_negativeId_returnNull() {
        assertTrue(orderDao.findById(-1).isEmpty());
    }

    @Test
    void findById_nonExistingId_returnNull() {
        assertTrue(orderDao.findById(1000).isEmpty());
    }

    @Test
    @Transactional
    void save_createOrder() {
        int size = (int) orderDao.count();
        order.setId(0);

        orderDao.save(order);
        assertEquals(orderDao.count(), ++size);
    }

    @Test
    void save_nullUser_throwException() {
        TestTransaction.end();
        order.setUser(null);

        assertThrows(DataAccessException.class, () -> orderDao.save(order));
    }

    @Test
    void save_nullCertificate_throwException() {
        TestTransaction.end();
        order.setUser(null);

        assertThrows(DataAccessException.class, () -> orderDao.save(order));
    }

    @Test
    void save_nullDate_throwException() {
        TestTransaction.end();
        order.setPurchaseDate(null);

        assertThrows(DataAccessException.class, () -> orderDao.save(order));
    }

    @Test
    void update_updateOrderOperation_ExceptionThrown() {
        order.setOrderAmount(BigDecimal.valueOf(120.0));
        orderDao.save(order);
        assertEquals(orderDao.findById(order.getId()).get(), order);
    }

    @Test
    void findByParameters_findByName_returnOrders() {
        OrderFinder finderMock = mock(OrderFinder.class);
        when(finderMock.getPredicate()).thenReturn(QOrder.order.certificate.id.eq(2));

        assertEquals(Collections.singletonList(order), orderDao.findAll(finderMock.getPredicate()));
    }

    @Test
    void delete_deleteOrder() {
        long size = orderDao.count();

        orderDao.delete(orderDao.findById(1).get());
        assertEquals(orderDao.count(), --size);
    }

    @Test
    void delete_nonExistingOrder_throwDataAccessException() {
        long size = orderDao.count();

        assertThrows(DataAccessException.class, () -> orderDao.delete(null));
        assertEquals(orderDao.count(), size);
    }
}
