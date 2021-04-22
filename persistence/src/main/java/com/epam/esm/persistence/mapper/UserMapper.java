package com.epam.esm.persistence.mapper;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.UserColumns;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User(resultSet.getString(UserColumns.FIRST_NAME.getValue()),
                resultSet.getString(UserColumns.LAST_NAME.getValue()),
                resultSet.getString(UserColumns.LOGIN.getValue()),
                resultSet.getString(UserColumns.PASSWORD.getValue()));
        user.setId(resultSet.getInt(UserColumns.ID.getValue()));
        return user;
    }
}
