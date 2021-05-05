package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.CertificateColumns;
import com.epam.esm.persistence.dao.certificate.CertificateDAOImpl;
import com.epam.esm.persistence.util.finder.impl.CertificateFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@Sql({"/SQL/test_db.sql"})
public class CertificateDaoTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CertificateDAOImpl certificateDao;

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
    void readAll_returnAll() {
        assertEquals(certificateDao.readAll().size(), 5);
    }

    @Test
    void read_returnCertificate() {
        assertEquals(certificate, certificateDao.read(1));
    }

    @Test
    void create_createCertificate() {
        int size = certificateDao.readAll().size();
        certificate.setId(0);

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
        assertThrows(DataAccessException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void create_nullCreateDate_throwException() {
        certificate.setCreateDate(null);
        assertThrows(DataAccessException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void create_nullLastUpdateDate_throwException() {
        certificate.setLastUpdateDate(null);
        assertThrows(DataAccessException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void update_updateCertificate() {
        int id = 3;
        certificate.setId(id);
        certificate = certificateDao.update(certificate);
        assertEquals(certificateDao.read(id), certificate);
    }

    @Test
    void delete_deleteCertificate() {
        int size = certificateDao.readAll().size();
        certificateDao.delete(2);
        assertEquals(certificateDao.readAll().size(), --size);
    }

    @Test
    void readBy_byFinder_returnCertificate() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
        Root<Certificate> root = query.from(Certificate.class);
        query.orderBy(builder.desc(root.get("name")));
        query = query.select(root);
        query.where(builder.equal(root.get(CertificateColumns.NAME.getValue()), "OZ.by"));

        CertificateFinder finderMock = mock(CertificateFinder.class);
        when(finderMock.getQuery()).thenReturn(query);

        assertEquals(Collections.singletonList(certificate), certificateDao.readBy(finderMock));
    }
}
