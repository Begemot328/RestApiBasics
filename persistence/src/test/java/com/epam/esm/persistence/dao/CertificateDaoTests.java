package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.dao.certificate.CertificateDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.CertificateMapper;
import com.epam.esm.persistence.util.CertificateFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CertificateDaoTests {
    private static CertificateDAOImpl certificateDao;
    private Certificate certificate;

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
    void init() {
        JdbcTemplate template = new JdbcTemplate(dataSource());
        certificateDao = new CertificateDAOImpl(template, new CertificateMapper());

        certificate = new Certificate("OZ.by",
                BigDecimal.valueOf(140.1), 10);
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22T09:20:11"));
        certificate.setCreateDate(LocalDateTime.parse("2021-03-22T09:20:11"));
        certificate.setId(1);
    }

    @Test
    void readAll_returnAll() {
        assertEquals(certificateDao.readAll().size(), 5);
    }

    @Test
    void read_returnCertificate() {
        assertEquals(certificateDao.read(1), certificate);
    }

    @Test
    void create_createCertificate() throws DAOSQLException {
        int size = certificateDao.readAll().size();

        certificateDao.create(certificate);
        assertEquals(certificateDao.readAll().size(), ++size);
    }

    @Test
    void create_nullName_throwException() {
        certificate.setName(null);
        assertThrows(DataAccessException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void create_nullPrice_throwException() {
        certificate.setPrice(null);
        assertThrows(DataIntegrityViolationException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void create_nullCreateDate_throwException() {
        certificate.setCreateDate(null);
        assertThrows(NullPointerException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void create_nullLastUpdateDate_throwException() {
        certificate.setLastUpdateDate(null);
        assertThrows(NullPointerException.class, () -> certificateDao.create(certificate));
    }


    @Test
    void update_updateCertificate() {
        int id = 3;
        certificate.setId(id);
        certificate = certificateDao.update(certificate);
        assertEquals(certificateDao.read(id), certificate);
    }

    @Test
    void readBy_byFinder_returnCertificate() {
        CertificateFinder finderMock = mock(CertificateFinder.class);
        when(finderMock.getQuery()).thenReturn(" WHERE NAME = 'OZ.by'");
        assertEquals(Collections.singletonList(certificate), certificateDao.readBy(finderMock));
    }

    @Test
    void delete_deleteCertificate() {
        int size = certificateDao.readAll().size();
        certificateDao.delete(2);
        assertEquals(certificateDao.readAll().size(), --size);
    }

    @Test
    void addCertificateTag_addTie() {
        assertTrue(certificateDao.isTagCertificateTied(1, 4));
    }

    @Test
    void testAddCertificateTag() {
        certificateDao.addCertificateTag(1, 1);
        assertTrue(certificateDao.isTagCertificateTied(1, 1));
    }

    @Test
    void deleteCertificateTag_deleteTie() {
        certificateDao.deleteCertificateTag(3, 5);
        assertFalse(certificateDao.isTagCertificateTied(3, 5));
    }
}
