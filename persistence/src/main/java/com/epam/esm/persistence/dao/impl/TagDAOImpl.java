package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.constants.TagQueries;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.mapper.TagMapper;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TagDAOImpl implements TagDAO {
    private final JdbcTemplate template;
    private final TagMapper tagMapper;


    @Autowired
    public TagDAOImpl(JdbcTemplate template, TagMapper tagMapper) {
        this.template = template;
        this.tagMapper = tagMapper;
    }

    @Override
    public Tag create(Tag tag) throws DAOSQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(TagQueries.INSERT_TAG.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder");
        }
        tag.setId(keyHolder.getKey().intValue());
        return tag;
    }

    @Override
    public Tag read(int id) {
        try {
            return template.queryForObject(
                    TagQueries.SELECT_FROM_TAG.getValue()
                            .concat(TagQueries.WHERE_ID.getValue()),
                    tagMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update operation for tag is unavailable");
    }

    @Override
    public void delete(int id) {
        template.update(TagQueries.DELETE_TAG.getValue(), id);
    }

    @Override
    public List<Tag> readAll() {
        return template.query(TagQueries.SELECT_FROM_TAG.getValue(), tagMapper);
    }

    @Override
    public List<Tag> readBy(EntityFinder<Tag> finder) {
        Stream<Tag> tagStream = template.queryForStream(TagQueries.SELECT_FROM_TAG_CERTIFICATES.getValue()
                        .concat(finder.getQuery()), tagMapper);
        List<Tag> tags = tagStream.distinct().collect(Collectors.toList());
        tagStream.close();
        return tags;
    }

    @Override
    public List<Tag> readBy(String query) {
        Stream<Tag> tagStream = template.queryForStream(query, tagMapper);
        List<Tag> tags = tagStream.distinct().collect(Collectors.toList());
        tagStream.close();
        return tags;
    }

    @Override
    public Tag readMostlyUsedTag() {
        List<Tag> tags = readBy(TagQueries.SELECT_MOST_POPULAR_TAG.getValue());

        if (CollectionUtils.isEmpty(tags)) {
            return null;
        }
        return tags.get(0);
    }
}
