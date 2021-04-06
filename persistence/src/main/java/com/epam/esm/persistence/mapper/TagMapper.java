package com.epam.esm.persistence.mapper;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Tag to/from DB mapper.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag(resultSet.getString(TagColumns.NAME.getValue()));
        tag.setId(resultSet.getInt(TagColumns.ID.getValue()));
        return tag;
    }
}
