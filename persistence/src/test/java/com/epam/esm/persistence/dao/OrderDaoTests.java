package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.OrderMapper;
import com.epam.esm.persistence.util.OrderFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderDaoTests {
    private static OrderDAOImpl orderDao;

    private Order order;
    private Certificate certificate;

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
    void init() {
        JdbcTemplate template = new JdbcTemplate(dataSource());
        orderDao = new OrderDAOImpl(template, new OrderMapper());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        certificate = new Certificate("nastolka.by",
                BigDecimal.valueOf(40.1), 50);
        certificate.setDescription("board games certificate");
        certificate.setCreateDate(LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        certificate.setId(2);
        User user = new User("Ivan", "Ivanov", "Ivanov", "qwerty");
        user.setId(2);
        order = new Order(certificate, user, BigDecimal.valueOf(150.0), 1,
                LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        order.setId(1);

    }

    @Test
    void readAll_returnAllOrders() {
        assertEquals(orderDao.readAll().size(), 6);
    }

    @Test
    void read_returnOrder() {
        assertEquals(orderDao.read(1), order);
    }

    @Test
    void read_negativeId_returnNull() {
        assertNull(orderDao.read(-1));
    }

    @Test
    void read_nonExistingId_returnNull() {
        assertNull(orderDao.read(1000));
    }

    @Test
    @Transactional
    void create_createOrder() throws DAOSQLException {
        int size = orderDao.readAll().size();
        order.setId(0);

        orderDao.create(order);
        assertEquals(orderDao.readAll().size(), ++size);
    }

    @Test
    void create_nullUser_throwException() {
        order.setUser(null);

        assertThrows(NullPointerException.class, () -> orderDao.create(order));
    }

    @Test
    void create_nullCertificate_throwException() {
        order.setUser(null);

        assertThrows(NullPointerException.class, () -> orderDao.create(order));
    }

    @Test
    void create_nullDate_throwException() {
        order.setPurchaseDate(null);

        assertThrows(NullPointerException.class, () -> orderDao.create(order));
    }

    @Test
    void update_updateOrderOperation_ExceptionThrown() {
        order.setOrderAmount(BigDecimal.valueOf(120.0));
        orderDao.update(order);
        assertEquals(order, orderDao.read(order.getId()));
    }

    @Test
    void readBy_readByName_returnOrders() {
        OrderFinder finderMock = mock(OrderFinder.class);

        when(finderMock.getQuery()).thenReturn(" WHERE user_id = 2");
        assertEquals(Collections.singletonList(order), orderDao.readBy(finderMock));
    }

    @Test
    void delete_deleteOrder() {
        int size = orderDao.readAll().size();

        orderDao.delete(1);
        assertEquals(orderDao.readAll().size(), --size);
    }

    @Test
    void delete_nonExistingOrder_doNothing() {
        int size = orderDao.readAll().size();

        orderDao.delete(1000);
        assertEquals(orderDao.readAll().size(), size);
    }
}
