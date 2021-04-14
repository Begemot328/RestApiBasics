package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.constants.CertificateQueries;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.mapper.CertificateMapper;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CertificateDAOImpl implements CertificateDAO {
    static Logger logger = LoggerFactory.getLogger(CertificateDAOImpl.class);

    private final JdbcTemplate template;
    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateDAOImpl(JdbcTemplate template, CertificateMapper certificateMapper) {
        this.template = template;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public Certificate create(Certificate certificate) throws DAOSQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CertificateQueries.INSERT_CERTIFICATE.getValue(),
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(certificate.getCreateDate()));
            ps.setTimestamp(6, Timestamp.valueOf(certificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DAOSQLException("empty keyholder");
        }
        certificate.setId(keyHolder.getKey().intValue());
        return certificate;
    }

    @Override
    public Certificate read(int id) {
        try {
            return template.queryForObject(CertificateQueries.SELECT_FROM_CERTIFICATE.getValue()
                            .concat(CertificateQueries.WHERE_ID.getValue()),
                    certificateMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Certificate update(Certificate certificate) {
        template.update(CertificateQueries.UPDATE_CERTIFICATE.getValue(), certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Timestamp.valueOf(certificate.getLastUpdateDate()),
                certificate.getId());
        return read(certificate.getId());
    }

    @Override
    public void delete(int id) {
        template.update(CertificateQueries.DELETE_CERTIFICATE.getValue(), id);
    }

    @Override
    public List<Certificate> readAll() {
        return template.query(CertificateQueries.SELECT_FROM_CERTIFICATE.getValue(), certificateMapper);
    }

    @Override
    public void addCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQueries.ADD_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    @Override
    public void deleteCertificateTag(int certificateId, int tagId) {
        template.update(CertificateQueries.DELETE_CERTIFICATE_TAG.getValue(),
                certificateId, tagId);
    }

    @Override
    public List<Certificate> readBy(EntityFinder<Certificate> finder) {
        Stream<Certificate> stream = template.queryForStream(CertificateQueries.SELECT_FROM_CERTIFICATE_TAG.getValue()
                        .concat(finder.getQuery()),
                certificateMapper);
        List<Certificate> result = stream.distinct().collect(Collectors.toList());
        stream.close();
        return result;
    }

    public boolean isTagCertificateTied(int certificateId, int tagId) {
        return template.queryForObject(CertificateQueries.COUNT_CERTIFICATE_TAG.getValue(),
                new Object[]{tagId, certificateId},
                new int[]{Types.INTEGER, Types.INTEGER},
                Integer.class) != 0;
    }
}
