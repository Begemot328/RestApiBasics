package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAOImpl;
import com.epam.esm.persistence.util.finder.impl.OrderFinder;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
class OrderDaoTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderDAOImpl orderDao;

    private Order order;

    @BeforeEach
    void init() {
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

        User user = new User("Ivan", "Ivanov", "Ivanov", "qwerty");
        user.setId(2);
        order = new Order(certificate, user, BigDecimal.valueOf(150.0), 1,
                LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        order.setId(1);
    }

    @Test
    void readAll_returnAllOrders() {
        assertEquals(6, orderDao.findAll().size());
    }

    @Test
    void read_returnOrder() {
        Order persistentOrder = orderDao.getById(1);
        assertEquals(persistentOrder, order);
    }

    @Test
    void read_negativeId_returnNull() {
        assertNull(orderDao.getById(-1));
    }

    @Test
    void read_nonExistingId_returnNull() {
        assertNull(orderDao.getById(1000));
    }

    @Test
    @Transactional
    void create_createOrder() {
        int size = orderDao.findAll().size();
        order.setId(0);

        orderDao.create(order);
        assertEquals(orderDao.findAll().size(), ++size);
    }

    @Test
    void create_nullUser_throwException() {
        order.setUser(null);

        assertThrows(DataAccessException.class, () -> orderDao.create(order));
    }

    @Test
    void create_nullCertificate_throwException() {
        order.setUser(null);

        assertThrows(DataAccessException.class, () -> orderDao.create(order));
    }

    @Test
    void create_nullDate_throwException() {
        order.setPurchaseDate(null);

        assertThrows(DataAccessException.class, () -> orderDao.create(order));
    }

    @Test
    void update_updateOrderOperation_ExceptionThrown() {
        order.setOrderAmount(BigDecimal.valueOf(120.0));
        orderDao.update(order);
        assertEquals(order, orderDao.getById(order.getId()));
    }

    @Test
    void readBy_readByName_returnOrders() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query = query.select(root);
        query.where(builder.equal(root.join("certificate").get("id"), "2"));

        OrderFinder finderMock = mock(OrderFinder.class);
        when(finderMock.getQuery()).thenReturn(query);

        assertEquals(Collections.singletonList(order), orderDao.findByParameters(finderMock));
    }

    @Test
    void delete_deleteOrder() {
        int size = orderDao.findAll().size();

        orderDao.delete(1);
        assertEquals(orderDao.findAll().size(), --size);
    }

    @Test
    void delete_nonExistingOrder_throwDataAccessException() {
        int size = orderDao.findAll().size();

        assertThrows(DataAccessException.class, () -> orderDao.delete(1000));
        assertEquals(orderDao.findAll().size(), size);
    }
}
