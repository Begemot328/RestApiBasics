package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.persistence.constants.CertificateQuerries;
import com.epam.esm.persistence.mapper.CertificateMapper;
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
import java.sql.*;
import java.util.Collection;
import java.util.List;

@Component
public class CertificateDAO implements EntityDAO<Certificate> {
    static Logger logger = LoggerFactory.getLogger(CertificateDAO.class);

    private JdbcTemplate template;
    private CertificateMapper certificateMapper;

    @Autowired
    public CertificateDAO(JdbcTemplate template, CertificateMapper certificateMapper) {
        this.template = template;
        this.certificateMapper = certificateMapper;
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
                        resultSet.getString(CertificateColumns.NAME.getValue()),
                        resultSet.getString(CertificateColumns.DESCRIPTION.getValue()),
                        resultSet.getBigDecimal(CertificateColumns.PRICE.getValue()),
                        resultSet.getInt(CertificateColumns.DURATION.getValue()),
                        resultSet.getDate(CertificateColumns.CREATE_DATE.getValue()).toLocalDate(),
                        resultSet.getDate(CertificateColumns.LAST_UPDATE_DATE.getValue()).toLocalDate()
                        );
                certificate.setId(resultSet.getInt(ID));
            }
            return certificate;
        }
    };

    @Override
    public Certificate create(Certificate certificate) throws DAOException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CertificateQuerries.INSERT_QUERY.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            ps.setDate(5, Date.valueOf(certificate.getCreateDate()));
            ps.setDate(6, Date.valueOf(certificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOException("empty keyholder");
        }
        certificate.setId(keyHolder.getKey().intValue());
        return certificate;
    }

    @Override
    public Certificate read(int id) throws DAOException {
        return template.queryForObject(CertificateQuerries.READ_QUERY.getValue()
                .concat(CertificateQuerries.WHERE_ID.getValue()
                        .replace("?", Integer.toString(id))),
                certificateMapper);
    }

    @Override
    public void update(Certificate certificate) throws DAOException {
        template.update(CertificateQuerries.UPDATE_QUERY.getValue(), certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Date.valueOf(certificate.getCreateDate()),
                Date.valueOf(certificate.getLastUpdateDate()),
                certificate.getId());
    }

    @Override
    public void delete(int id) throws DAOException {
        template.update(CertificateQuerries.DELETE_QUERY.getValue(), id);
    }

    @Override
    public List<Certificate> findAll() throws DAOException {
        return template.query(CertificateQuerries.READ_QUERY.getValue(), certificateMapper);
    }

    public Collection<Certificate> findAllByTag(Tag tag) throws DAOException {
        return template.query(CertificateQuerries.READ_BY_TAG_QUERY.getValue()
                        .concat(CertificateQuerries.WHERE_TAG_ID.getValue()
                                .replace("?", Integer.toString(tag.getId()))),
                certificateMapper);
    }

    public void addCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQuerries.ADD_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    public void deleteCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQuerries.DELETE_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    public Collection<Certificate> findBy(EntityFinder<Certificate> finder) throws DAOException {

        return template.query(CertificateQuerries.READ_BY_TAG_QUERY.getValue()
                        .concat(finder.getQuery()),
                certificateMapper);
    }
}
