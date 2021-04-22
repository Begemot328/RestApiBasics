package com.epam.esm.persistence.dao.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.CertificateQueries;
import com.epam.esm.persistence.constants.OrderQueries;
import com.epam.esm.persistence.constants.OrderColumns;
import com.epam.esm.persistence.dao.OrderDAO;
import com.epam.esm.persistence.dao.UserDAO;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.OrderMapper;
import com.epam.esm.persistence.mapper.UserMapper;
import com.epam.esm.persistence.util.EntityFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class OrderDAOImpl implements OrderDAO {
    private final JdbcTemplate template;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderDAOImpl(JdbcTemplate template, OrderMapper orderMapper) {
        this.template = template;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order create(Order order) throws DAOSQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(OrderQueries.INSERT_ORDER.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(OrderColumns.AMOUNT.getColumn(), order.getAmount());
            ps.setInt(OrderColumns.QUANTITY.getColumn(), order.getQuantity());
            ps.setInt(OrderColumns.USER_ID.getColumn(), order.getUser().getId());
            ps.setInt(OrderColumns.CERTIFICATE_ID.getColumn(), order.getCertificate().getId());
            ps.setTimestamp(OrderColumns.PURCHASE_DATE.getColumn(),
                    Timestamp.valueOf(order.getPurchaseDate()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder");
        }
        order.setId(keyHolder.getKey().intValue());
        return order;
    }

    @Override
    public Order read(int id) {
        try {
            return template.queryForObject(
                    OrderQueries.SELECT_FROM_ORDER.getValue()
                            .concat(OrderQueries.WHERE_ID.getValue()),
                    orderMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Order update(Order order) {
        template.update(OrderQueries.UPDATE_ORDER.getValue(), order.getUser(),
                order.getCertificate(),
                order.getPurchaseDate(),
                order.getAmount(),
                order.getQuantity());
        return read(order.getId());
    }

    @Override
    public void delete(int id) {
        template.update(OrderQueries.DELETE_ORDER.getValue(), id);
    }

    @Override
    public List<Order> readAll() {
        return template.query(OrderQueries.SELECT_FROM_ORDER.getValue(), orderMapper);
    }

    @Override
    public List<Order> readBy(EntityFinder<Order> finder) {
        Stream<Order> orderStream = template.queryForStream(OrderQueries.SELECT_FROM_ORDER.getValue()
                .concat(finder.getQuery()), orderMapper);
        List<Order> orders = orderStream.distinct().collect(Collectors.toList());
        orderStream.close();
        return orders;
    }
}
