package com.epam.esm.persistence.dao.certificate;

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

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CertificateDAO extends AbstractEntityDAO<Certificate> {
    private static final String WHERE_TAG_ID = " WHERE tag_id = ?";
    static final String TAG_ID = "tag_id";
    private static final String READ_BY_TAG_QUERY = "SELECT * FROM tag_certificates";
    private static final String WHERE_ID = " WHERE id = ?";
    private static final String READ_QUERY = "SELECT * FROM certificates.certificate";
    private static final String INSERT_QUERY =
            "INSERT INTO certificates.certificate (name, description, price," +
                    "duration, create_date, last_update_date) VALUES (?,?,?,?,?,?);";
    private static final String UPDATE_QUERY =
            "UPDATE certificates.certificate SET name=?, description=?, price=?," +
                    "duration=?, create_date=?, last_update_date=? " +
                    "WHERE id = ?;";
    private static final String DELETE_QUERY =
            "DELETE FROM certificates.certificate  " +
                    "WHERE id = ?;";
    private static final String ADD_CERTIFICATE_TAG = "INSERT INTO certificates.certificate_tag " +
            "(certificate_id, tag_id) VALUES (?,?);";
    private static final String DELETE_CERTIFICATE_TAG = "DELETE FROM certificates.certificate_tag " +
            "WHERE certificate_id=? AND tag_id=?;";

    static final String DESCRIPTION = "description";
    static final String NAME = "name";
    static final String DURATION = "duration";
    static final String PRICE = "price";
    static final String ID = "id";
    static final String LAST_UPDATE_DATE = "last_update_date";
    static final String CREATE_DATE = "create_date";
    private JdbcTemplate template;

    @Autowired
    public CertificateDAO(JdbcTemplate template) {
        this.template = template;
    }


    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public JdbcTemplate getTemplate() {
        return this.template;
    }

    private final ResultSetExtractor<Certificate> certificateExtractor = new ResultSetExtractor<Certificate>() {
        @Override
        public Certificate extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Certificate certificate = null;

            if(resultSet.next()) {
                certificate = new Certificate(
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getDouble(PRICE),
                        resultSet.getInt(DURATION),
                        resultSet.getDate(CREATE_DATE).toLocalDate(),
                        resultSet.getDate(LAST_UPDATE_DATE).toLocalDate()
                        );
                certificate.setId(resultSet.getInt(ID));
            }
            return certificate;
        }
    };

    private final ResultSetExtractor<List<Certificate>> certificateListExtractor = new ResultSetExtractor<List<Certificate>>() {
        @Override
        public List<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Certificate certificate = null;
            List<Certificate> list = new ArrayList<>();

            while (resultSet.next()) {
                certificate = new Certificate(
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getDouble(PRICE),
                        resultSet.getInt(DURATION),
                        resultSet.getDate(CREATE_DATE).toLocalDate(),
                        resultSet.getDate(LAST_UPDATE_DATE).toLocalDate()
                );
                certificate.setId(resultSet.getInt(ID));
                if (!list.contains(certificate)) {
                    list.add(certificate);
                }
            }
            return list;
        }
    };

    @Override
    public Certificate create(Certificate certificate) throws DAOException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setDouble(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            ps.setDate(5, Date.valueOf(certificate.getCreateDate()));
            ps.setDate(6, Date.valueOf(certificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);
        certificate.setId(keyHolder.getKey().intValue());
        return certificate;
    }

    @Override
    public Certificate read(int id) throws DAOException {
        return getTemplate().query(READ_QUERY
                .concat(WHERE_ID.replace("?", Integer.toString(id))),
                        certificateExtractor);
    }

    @Override
    public void update(Certificate certificate) throws DAOException {
        template.update(UPDATE_QUERY, certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Date.valueOf(certificate.getCreateDate()),
                Date.valueOf(certificate.getLastUpdateDate()),
                certificate.getId());
    }

    @Override
    public void delete(int id) throws DAOException {
        template.update(DELETE_QUERY, id);
    }

    @Override
    public Collection<Certificate> findAll() throws DAOException {
        return getTemplate().query(READ_QUERY, certificateListExtractor);
    }

    public Collection<Certificate> findAllByTag(Tag tag) throws DAOException {
        return getTemplate().query(READ_BY_TAG_QUERY
                        .concat(WHERE_TAG_ID
                                .replace("?", Integer.toString(tag.getId()))),
                certificateListExtractor);
    }

    public void addCertificateTag(Certificate certificate, Tag tag) {
        template.update(ADD_CERTIFICATE_TAG, certificate.getId(), tag.getId());
    }

    public void deleteCertificateTag(Certificate certificate, Tag tag) {
        template.update(DELETE_CERTIFICATE_TAG, certificate.getId(), tag.getId());
    }

    public Collection<Certificate> findBy(EntityFinder<Certificate> finder) throws DAOException {
        System.out.println(READ_BY_TAG_QUERY
                        .concat(finder.getQuery()));
        return getTemplate().query(READ_BY_TAG_QUERY
                        .concat(finder.getQuery()),
                certificateListExtractor);
    }
}
