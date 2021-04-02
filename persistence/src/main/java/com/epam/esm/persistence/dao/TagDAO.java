package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.constants.TagQuerries;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class TagDAO implements EntityDAO<Tag> {
    static Logger logger = LoggerFactory.getLogger(TagDAO.class);
    private JdbcTemplate template;
    private TagMapper tagMapper;

    public TagDAO() {}

    @Autowired
    public TagDAO(JdbcTemplate template, TagMapper tagMapper) {
        this.template = template;
        this.tagMapper = tagMapper;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Tag create(Tag tag) throws DAOException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(TagQuerries.INSERT_QUERY.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOException("empty keyholder");
        }
        tag.setId(keyHolder.getKey().intValue());
        return   tag;
    }

    @Override
    public Tag read(int id) throws DAOException {
        return template.queryForObject(
                TagQuerries.READ_QUERY.getValue()
                .concat(TagQuerries.WHERE_ID.getValue().replace("?",
                        Integer.toString(id))),
        tagMapper);
    }

    @Override
    public void update(Tag tag) throws DAOException {
       throw new UnsupportedOperationException("Update operation for tag in unavailable");
    }

    @Override
    public void delete(int id) throws DAOException {
        template.update(TagQuerries.DELETE_QUERY.getValue(), id);
    }

    @Override
    public List<Tag> findAll() throws DAOException {
        return template.query(TagQuerries.READ_QUERY.getValue(), tagMapper);
    }

    public Collection<Tag> findBy(EntityFinder<Tag> finder) throws DAOException {
        System.out.println(TagQuerries.READ_BY_TAG_QUERY.getValue()
                .concat(finder.getQuery()));
        return template.query(TagQuerries.READ_BY_TAG_QUERY.getValue()
                        .concat(finder.getQuery()),
                tagMapper);
    }
}
