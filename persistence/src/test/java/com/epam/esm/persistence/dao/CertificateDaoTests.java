package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.QCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.util.finder.impl.CertificateFinder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(classes = PersistenceTestConfig.class)
@Sql({"/SQL/test_db.sql"})
class CertificateDaoTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CertificateDAO certificateDao;

    private Certificate certificate;

    @BeforeEach
    void init() {
        Tag tag1 = new Tag("books");
        tag1.setId(3);
        Tag tag2 = new Tag("films");
        tag2.setId(4);
        certificate = new Certificate("OZ.by",
                BigDecimal.valueOf(140.1), 10);
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22T09:20:11"));
        certificate.setCreateDate(LocalDateTime.parse("2021-03-22T09:20:11"));
        certificate.setId(1);

        certificate.setTags(Arrays.asList(tag1, tag2));
    }

    @Test
    void findAll_returnAll() {
        assertEquals(5, IterableUtils.toList(certificateDao.findAll()).size());
    }

    @Test
    void read_returnCertificate() {
        assertEquals(certificate, certificateDao.findById(1).get());
    }

    @Test
    void save_saveCertificate() {
        long size = certificateDao.count();
        certificate.setId(0);
        certificateDao.save(certificate);
        assertEquals(certificateDao.count(), ++size);
    }

    @Test
    void save_nullName_throwException() {
        TestTransaction.end();
        certificate.setName(null);
        assertThrows(DataAccessException.class, () -> certificateDao.save(certificate));
    }

    @Test
    void save_nullPrice_throwException() {
        TestTransaction.end();
        certificate.setPrice(null);
        assertThrows(DataAccessException.class, () -> certificateDao.save(certificate));
    }

    @Test
    void save_nullCreateDate_throwException() {
        TestTransaction.end();
        certificate.setCreateDate(null);
        assertThrows(DataAccessException.class, () -> certificateDao.save(certificate));
    }

    @Test
    void save_nullLastUpdateDate_throwException() {
        TestTransaction.end();
        certificate.setLastUpdateDate(null);
        assertThrows(DataAccessException.class, () -> certificateDao.save(certificate));
    }

    @Test
    void update_updateCertificate() {
        int id = 3;
        certificate.setId(id);
        certificate = certificateDao.save(certificate);

        assertEquals(certificateDao.findById(id).get(), certificate);
    }

    @Test
    void delete_deleteCertificate() {
        long size = certificateDao.count();
        certificateDao.delete(certificateDao.findById(1).get());
        assertEquals(certificateDao.count(), --size);
    }

    @Test
    void findByParameters_findByFinder_returnCertificate() {
        CertificateFinder finderMock = mock(CertificateFinder.class);
        when(finderMock.getPredicate()).thenReturn(QCertificate.certificate.name.eq("OZ.by"));

        assertEquals(Collections.singletonList(certificate),
                certificateDao.findAll(finderMock.getPredicate()));
    }
}
