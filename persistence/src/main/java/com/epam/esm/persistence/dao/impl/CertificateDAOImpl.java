package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.constants.CertificateQuerries;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.mapper.CertificateMapper;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateDAOImpl implements CertificateDAO {
    static Logger logger = LoggerFactory.getLogger(CertificateDAOImpl.class);

    private JdbcTemplate template;
    private CertificateMapper certificateMapper;

    @Autowired
    public CertificateDAOImpl(JdbcTemplate template, CertificateMapper certificateMapper) {
        this.template = template;
        this.certificateMapper = certificateMapper;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public JdbcTemplate getTemplate() {
        return this.template;
    }

    @Override
    public Certificate create(Certificate certificate) throws DAOSQLException {
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
            throw new DAOSQLException("empty keyholder");
        }
        certificate.setId(keyHolder.getKey().intValue());
        return certificate;
    }

    @Override
    public Certificate read(int id) throws DAOSQLException {
        List<Certificate> result = template.query(CertificateQuerries.READ_QUERY.getValue()
                .concat(CertificateQuerries.WHERE_ID.getValue()
                        .replace("?", Integer.toString(id))),
                certificateMapper);
        if(result.isEmpty()) {
            return  null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void update(Certificate certificate) throws DAOSQLException {
        template.update(CertificateQuerries.UPDATE_QUERY.getValue(), certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Date.valueOf(certificate.getCreateDate()),
                Date.valueOf(certificate.getLastUpdateDate()),
                certificate.getId());
    }

    @Override
    public void delete(int id) throws DAOSQLException {
        template.update(CertificateQuerries.DELETE_QUERY.getValue(), id);
    }

    @Override
    public List<Certificate> findAll() throws DAOSQLException {
        return template.query(CertificateQuerries.READ_QUERY.getValue(), certificateMapper);
    }

    @Override
    public void addCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQuerries.ADD_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    @Override
    public void deleteCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQuerries.DELETE_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    @Override
    public List<Certificate> findBy(EntityFinder<Certificate> finder) throws DAOSQLException {
        return template.queryForStream(CertificateQuerries.READ_BY_TAG_QUERY.getValue()
                        .concat(finder.getQuery()),
                certificateMapper).distinct().collect(Collectors.toList());
    }

    public boolean iStagCertificateTied(int certificateId, int tagId) {
        if (template.queryForObject(CertificateQuerries.CHECK_RELATION_BY_TAG.getValue()
                + tagId + CertificateQuerries.CHECK_RELATION_BY_CERTIFICATE.getValue()
                + certificateId + ";", Integer.class) == 0) {
            return false;
        } else  return true;
    }
}
