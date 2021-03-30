package com.epam.esm.persistence.dao.tag;

import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Certificate;
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

    @Autowired
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
                tag = new Tag(resultSet.getString(TagColumns.NAME.getValue()));
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
                tag = new Tag(resultSet.getString(TagColumns.NAME.getValue()));
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
                    .prepareStatement(TagQuerries.INSERT_QUERY.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            logger.error("empty keyholder");
            throw new DAOException("empty keyholder");
        }
        tag.setId(keyHolder.getKey().intValue());
        return   tag;
    }

    @Override
    public Tag read(int id) throws DAOException {
        return getTemplate().query(TagQuerries.READ_QUERY.getValue()
                .concat(TagQuerries.WHERE_ID.getValue().replace("?", Integer.toString(id))),
                        tagExtractor);
    }

    @Override
    public void update(Tag tag) throws DAOException {
        template.update(TagQuerries.UPDATE_QUERY.getValue(), tag.getName(), tag.getId());
    }

    @Override
    public void delete(int id) throws DAOException {
        template.update(TagQuerries.DELETE_QUERY.getValue(), id);
    }

    @Override
    public Collection<Tag> findAll() throws DAOException {
        return getTemplate().query(TagQuerries.READ_QUERY.getValue(), tagListExtractor);
    }


    public Collection<Tag> findAllByCertificate(Certificate certificate) throws DAOException {
        return getTemplate().query(TagQuerries.READ_BY_TAG_QUERY.getValue()
                .concat(TagQuerries.WHERE_CERTIFICATE_ID.getValue()
                        .replace("?", Integer.toString(certificate.getId()))),
                tagListExtractor);
    }

    public Collection<Tag> findBy(EntityFinder<Tag> finder) throws DAOException {
        System.out.println(TagQuerries.READ_BY_TAG_QUERY.getValue()
                .concat(finder.getQuery()));
        return getTemplate().query(TagQuerries.READ_BY_TAG_QUERY.getValue()
                        .concat(finder.getQuery()),
                tagListExtractor);
    }
}
