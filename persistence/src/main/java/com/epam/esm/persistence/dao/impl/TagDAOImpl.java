package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.constants.TagQuerries;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagDAOImpl implements TagDAO {
    static Logger logger = LoggerFactory.getLogger(TagDAOImpl.class);
    private JdbcTemplate template;
    private TagMapper tagMapper;

    public TagDAOImpl() {}

    @Autowired
    public TagDAOImpl(JdbcTemplate template, TagMapper tagMapper) {
        this.template = template;
        this.tagMapper = tagMapper;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Tag create(Tag tag) throws DAOSQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(TagQuerries.INSERT_QUERY.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder");
        }
        tag.setId(keyHolder.getKey().intValue());
        return   tag;
    }

    @Override
    public Tag read(int id) throws DAOSQLException {
        List<Tag> result =  template.query(
                TagQuerries.READ_QUERY.getValue()
                .concat(TagQuerries.WHERE_ID.getValue().replace("?",
                        Integer.toString(id))),
        tagMapper);
        if(result.isEmpty()) {
            return  null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void update(Tag tag) throws DAOSQLException {
       throw new UnsupportedOperationException("Update operation for tag in unavailable");
    }

    @Override
    public void delete(int id) throws DAOSQLException {
        template.update(TagQuerries.DELETE_QUERY.getValue(), id);
    }

    @Override
    public List<Tag> findAll() throws DAOSQLException {
        return template.query(TagQuerries.READ_QUERY.getValue(), tagMapper);
    }

    @Override
    public List<Tag> findBy(EntityFinder<Tag> finder) throws DAOSQLException {
        return template.queryForStream(TagQuerries.READ_BY_CERTIFICATE_QUERY.getValue()
                        .concat(finder.getQuery()),
                tagMapper).distinct().collect(Collectors.toList());
    }
}
