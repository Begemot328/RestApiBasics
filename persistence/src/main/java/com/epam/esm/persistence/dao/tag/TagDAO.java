package com.epam.esm.persistence.dao.tag;

import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.persistence.dao.AbstractEntityDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOException;
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
public class TagDAO extends AbstractEntityDAO<Tag> {
    private static final String WHERE_ID = " WHERE id = ?";
    private static final String WHERE_CERTIFICATE_ID = " WHERE certificate_id = ?";
    static final String CERTIFICATE_ID = "certificate_id";
    private static final String READ_QUERY = "SELECT * FROM certificates.tag";
    private static final String READ_BY_TAG_QUERY = "SELECT * FROM certificate_tags";
    private static final String INSERT_QUERY =
            "INSERT INTO certificates.tag (name) VALUES (?);";
    private static final String UPDATE_QUERY =
            "UPDATE certificates.tag  SET name = ? " +
                    "WHERE id = ?;";
    private static final String DELETE_QUERY =
            "DELETE FROM certificates.tag  " +
                    "WHERE id = ?;";
    static final String NAME = "name";

    @Autowired
    private JdbcTemplate template;


    public TagDAO(JdbcTemplate template) {
        this.template = template;
    }


    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public JdbcTemplate getTemplate() {
        return this.template;
    }

    private ResultSetExtractor<Tag> tagExtractor = new ResultSetExtractor<Tag>() {
        @Override
        public Tag extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Tag tag = null;
            String name;
            int id;
            if(resultSet.next()) {
                tag = new Tag(resultSet.getString(NAME));
                tag.setId(resultSet.getInt(ID));
            }
            return tag;
        }
    };

    private ResultSetExtractor<List<Tag>> tagListExtractor = new ResultSetExtractor<List<Tag>>() {
        @Override
        public List<Tag> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Tag tag = null;
            List<Tag> list = new ArrayList<>();
            String name;
            int id;
            while (resultSet.next()) {
                tag = new Tag(resultSet.getString(NAME));
                tag.setId(resultSet.getInt(ID));
                if (!list.contains(tag)) {
                    list.add(tag);
                }

            }
            return list;
        }
    };

    @Override
    public Tag create(Tag tag) throws DAOException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        tag.setId(keyHolder.getKey().intValue());
        return   tag;
    }

    @Override
    public Tag read(int id) throws DAOException {
        return getTemplate().query(READ_QUERY
                .concat(WHERE_ID.replace("?", Integer.toString(id))),
                        tagExtractor);
    }

    @Override
    public void update(Tag tag) throws DAOException {
        template.update(UPDATE_QUERY, tag.getName(), tag.getId());
    }

    @Override
    public void delete(int id) throws DAOException {
        template.update(DELETE_QUERY, id);
    }

    @Override
    public Collection<Tag> findAll() throws DAOException {
        return getTemplate().query(READ_QUERY, tagListExtractor);
    }


    public Collection<Tag> findAllByCertificate(Certificate certificate) throws DAOException {
        return getTemplate().query(READ_BY_TAG_QUERY
                .concat(WHERE_CERTIFICATE_ID
                        .replace("?", Integer.toString(certificate.getId()))),
                tagListExtractor);
    }

    public Collection<Tag> findBy(EntityFinder<Tag> finder) throws DAOException {
        System.out.println(READ_BY_TAG_QUERY
                .concat(finder.getQuery()));
        return getTemplate().query(READ_BY_TAG_QUERY
                        .concat(finder.getQuery()),
                tagListExtractor);
    }
}
