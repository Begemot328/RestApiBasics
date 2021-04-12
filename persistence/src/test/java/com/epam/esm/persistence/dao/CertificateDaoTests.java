package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.CertificateDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.CertificateMapper;
import com.epam.esm.persistence.util.CertificateFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;

public class CertificateDaoTests {
    private static JdbcTemplate template;
    private static CertificateDAOImpl certificateDao;
    Tag tag = new Tag("New tag");
    Certificate certificate  = new Certificate("Certificate1",
            null, BigDecimal.valueOf(10.0), 3, null, null);

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:SQL/test_db.sql").build();
    }

    @BeforeEach
     void init() {
        template = new JdbcTemplate(dataSource());
        certificateDao = new CertificateDAOImpl(template, new CertificateMapper());
    }

    @Test
    void testFindAll() throws DAOSQLException {
        assertEquals(certificateDao.readAll().size(), 5);
    }

    @Test
    void testRead() throws DAOSQLException {
        Certificate certificate = new Certificate("OZ.by", null,
                BigDecimal.valueOf(140.1), 10,
                LocalDateTime.parse("2021-03-22T09:20:11"),
                LocalDateTime.parse("2021-03-22T09:20:11") );
        certificate.setId(1);
        assertEquals(certificateDao.read(1), certificate);
    }

    @Test
    void testCreate() throws DAOSQLException {
        Certificate certificate = new Certificate("OZ.by", null,
                BigDecimal.valueOf(140.1), 10,
                LocalDateTime.parse("2021-03-22T06:12:15.156"),
                LocalDateTime.parse("2021-03-22T06:12:15.156") );
        int size = certificateDao.readAll().size();

        certificateDao.create(certificate);
        assertEquals(certificateDao.readAll().size(), ++size);
    }

    @Test
    void testUpdate() throws DAOSQLException {
        Certificate certificate = new Certificate("OZ.by", null,
                BigDecimal.valueOf(140.1), 10,
                LocalDateTime.parse("2021-03-22T06:12:15.156"), LocalDateTime.parse("2021-03-22T06:12:15.156") );
        int id = 3;
        certificate.setId(id);
        certificateDao.update(certificate);
        assertEquals(certificateDao.read(id), certificate);
    }

    @Test
    void testFindBy() throws DAOSQLException {
        CertificateFinder finderMock = mock(CertificateFinder.class);
        when(finderMock.getQuery()).thenReturn(" WHERE NAME = 'OZ.by'");
        Certificate certificate = new Certificate("OZ.by", null,
                BigDecimal.valueOf(140.1), 10,
                LocalDateTime.parse("2021-03-22T09:20:11"), LocalDateTime.parse("2021-03-22T09:20:11") );
        certificate.setId(1);
        assertEquals(Collections.singletonList(certificate), certificateDao.readBy(finderMock));
    }

    @Test
    void testDelete() throws DAOSQLException {
        int size = certificateDao.readAll().size();

        certificateDao.delete(2);
        assertEquals(certificateDao.readAll().size(), --size);
    }

    @Test
    void testIsTagCertificateTied() {
        assertTrue(certificateDao.isTagCertificateTied(1, 4));
    }

    @Test
    void testAddCertificateTag() {
        certificateDao.addCertificateTag(1,1);
        assertTrue(certificateDao.isTagCertificateTied(1, 1));
    }

    @Test
    void testDeleteCertificateTag() {
        certificateDao.deleteCertificateTag(3,5);
        assertFalse(certificateDao.isTagCertificateTied(3, 5));
    }
}
