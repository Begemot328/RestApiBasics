package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.CertificateDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.mapper.CertificateMapper;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.impl.CertificateServiceImpl;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CertificateServiceImplTests {
    private static CertificateDAOImpl certificateDaoMock;
    private static  EntityValidator<Certificate> validator;
    private static  CertificateFinder finder = new CertificateFinder();
    private static  CertificateServiceImpl service;
    private static  TagServiceImpl tagServiceMock;

    private static  Tag tag1 = new Tag("Tag1");
    private static  Certificate certificate1 = new Certificate("Certificate1",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private static  Certificate certificate2 = new Certificate("Certificate2",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private static  Certificate certificate3 = new Certificate("Certificate3",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private static  Certificate certificate4 = new Certificate("Certificate4",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private static  Certificate[] certificates = {certificate1, certificate2, certificate3, certificate4};
    private static  Certificate[] certificatesShort = {certificate1, certificate2};
    private static  List<Certificate> fullList = Arrays.asList(certificates);
    private static  List<Certificate> shortList = Arrays.asList(certificatesShort);


    @BeforeEach
    void init() {
        certificateDaoMock = mock(CertificateDAOImpl.class);
        tagServiceMock = mock(TagServiceImpl.class);
            try {
                when(tagServiceMock.read(any(Integer.class))).thenReturn(tag1);
                when(tagServiceMock.create(any(Tag.class))).thenAnswer(invocation -> {
                    Tag tag = invocation.getArgument(0, Tag.class);
                    tag.setId(tag.getId() + 1);
                    return tag;
                });

                when(certificateDaoMock.readAll()).thenReturn(fullList);
                when(certificateDaoMock.read(1)).thenReturn(certificate1);
                when(certificateDaoMock.read(2)).thenReturn(certificate2);
                when(certificateDaoMock.readBy(any(CertificateFinder.class))).thenReturn(shortList);
                doNothing().when(certificateDaoMock).delete(any(Integer.class));
                doNothing().when(certificateDaoMock).update(any(Certificate.class));
                when(certificateDaoMock.create(any(Certificate.class))).thenAnswer(invocation -> {
                    Certificate certificate = invocation.getArgument(0, Certificate.class);
                    certificate.setId(fullList.size());
                    return certificate;
                });
                when(certificateDaoMock.isTagCertificateTied(
                        any(Integer.class), any(Integer.class)))
                        .thenReturn(false);
                when(certificateDaoMock.isTagCertificateTied(1, 3)).thenReturn(true);

                validator = mock(CertificateValidator.class);
                doNothing().when(validator).validate(any(Certificate.class));
            } catch (DAOSQLException | ServiceException | ValidationException e) {
                e.printStackTrace();
            }
            certificate1.setId(1);
            certificate2.setId(2);
            certificate1.setId(3);
            certificate2.setId(4);
            service = new CertificateServiceImpl(certificateDaoMock, validator, tagServiceMock);

    }

    @Test
    public void readTest() throws ServiceException {
        assertEquals(certificate1, service.read(1));
    }

    @Test
    public void findAllTest() throws ServiceException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void createTest() throws ServiceException, ValidationException, DAOSQLException {
        Certificate certificate = service.create(certificate1);
        assertEquals(certificate.getId(), fullList.size());
        verify(certificateDaoMock, times(1)).create(certificate1);
    }

    @Test
    public void deleteTest() throws ServiceException, DAOSQLException {
        service.delete(1);
        verify(certificateDaoMock, times(1)).read(1);
    }

    @Test
    public void updateTest() throws ServiceException, ValidationException, DAOSQLException {
        service.update(certificate2);
        verify(certificateDaoMock, times(1)).update(certificate2);
    }

    @Test
    public void findByTagTest() throws ServiceException, DAOSQLException {
        assertEquals(shortList, service.readByTag(1));
        CertificateFinder finder = new CertificateFinder();
        finder.findByTag(1);
        verify(certificateDaoMock, times(1)).readBy(finder);
    }

    @Test
    public void findTest() throws ServiceException, DAOSQLException, BadRequestException {
        Map<String, String> params = new HashMap<>();
        params.put(CertificateSearchParameters.NAME.name(), "1");
        params.put(CertificateSortingParameters.SORT_BY_NAME.name().toLowerCase(), "2");

        assertEquals(shortList, service.read(params));
        verify(certificateDaoMock, times(1)).readBy(any(CertificateFinder.class));
    }

    @Test
    public void addCertificateTagTest() throws ServiceException, ValidationException, BadRequestException {
        service.addCertificateTag(1, tag1);
        verify(certificateDaoMock, times(1))
                .addCertificateTag(1, tag1.getId());
    }

    @Test
    public void addCertificateTag2Test() throws ServiceException, ValidationException, BadRequestException {
        service.addCertificateTag(certificate1, 1);
        verify(certificateDaoMock, times(1))
                .addCertificateTag(certificate1.getId(), 1);
    }

    @Test
    public void deleteCertificateTagTest() throws BadRequestException {
        service.deleteCertificateTag(1, 3);
        verify(certificateDaoMock, times(1))
                .deleteCertificateTag(1, 3);
    }
}
