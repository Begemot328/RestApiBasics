package com.epam.esm.persistence.mapper;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag(resultSet.getString(TagColumns.NAME.getValue()));
        tag.setId(resultSet.getInt(TagColumns.ID.getValue()));
        return tag;
    }
}
