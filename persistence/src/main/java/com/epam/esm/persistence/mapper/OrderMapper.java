package com.epam.esm.persistence.mapper;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.OrderColumns;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

@Component
public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new CertificateMapper().mapRow(resultSet, i);
        certificate.setId(resultSet.getInt(OrderColumns.CERTIFICATE_ID.getValue()));
        User user = new UserMapper().mapRow(resultSet, i);
        user.setId(resultSet.getInt(OrderColumns.CERTIFICATE_ID.getValue()));
        Order order = new Order(certificate, user,
                BigDecimal.valueOf(resultSet.getFloat(OrderColumns.AMOUNT.getValue())),
                resultSet.getInt(OrderColumns.QUANTITY.getValue()),
                resultSet.getTimestamp(OrderColumns.PURCHASE_DATE.getValue())
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        order.setId(resultSet.getInt(OrderColumns.ID.getValue()));
        return order;
    }
}
