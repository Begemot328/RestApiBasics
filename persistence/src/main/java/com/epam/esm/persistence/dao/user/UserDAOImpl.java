package com.epam.esm.persistence.dao.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.constants.UserColumns;
import com.epam.esm.persistence.constants.UserQueries;
import com.epam.esm.persistence.exceptions.DAOSQLException;
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
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private final JdbcTemplate template;
    private final UserMapper userMapper;

    @Autowired
    public UserDAOImpl(JdbcTemplate template, UserMapper userMapper) {
        this.template = template;
        this.userMapper = userMapper;
    }

    @Override
    public User create(User user) throws DAOSQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int id;
        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(UserQueries.INSERT_USER.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(UserColumns.FIRST_NAME.getColumn(), user.getFirstName());
            ps.setString(UserColumns.LAST_NAME.getColumn(), user.getLastName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder");
        }
        id = keyHolder.getKey().intValue();
        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(UserQueries.INSERT_ACCOUNT.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder!");
        }
        if (id != keyHolder.getKey().intValue()) {
            throw new DAOSQLException("non-coherent tables 'user' and 'account'");
        }
        user.setId(id);
        return user;
    }

    @Override
    public User read(int id) {
        try {
            return template.queryForObject(
                    UserQueries.SELECT_FROM_USER.getValue()
                            .concat(UserQueries.WHERE_ID.getValue()),
                    userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User update(User user) {
        template.update(UserQueries.UPDATE_USER.getValue(), user.getFirstName(),
                user.getLastName(), user.getId());
        template.update(UserQueries.UPDATE_ACCOUNT.getValue(),
                user.getLogin(),
                user.getPassword(), user.getId());
        return read(user.getId());
    }

    @Override
    public void delete(int id) {
        template.update(UserQueries.DELETE_USER.getValue(), id);
    }

    @Override
    public List<User> readAll() {
        return template.query(UserQueries.SELECT_FROM_USER.getValue(), userMapper);
    }

    @Override
    public String getPassword(String login) {
        return template.queryForObject(UserQueries.SELECT_PASSWORD.getValue(), String.class, login);
    }

    @Override
    public List<User> readBy(EntityFinder<User> finder) {
        return template.query(UserQueries.SELECT_FROM_USER.getValue()
                .concat(finder.getQuery()), userMapper);
    }
}
