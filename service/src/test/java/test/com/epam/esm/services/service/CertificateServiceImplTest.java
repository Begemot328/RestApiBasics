package test.com.epam.esm.services.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.impl.CertificateDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.services.constants.CertificateSearchParameters;
import com.epam.esm.services.constants.CertificateSortingParameters;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.impl.CertificateServiceImpl;
import com.epam.esm.services.service.impl.TagServiceImpl;
import com.epam.esm.services.validator.EntityValidator;
import com.epam.esm.services.validator.CertificateValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CertificateServiceImplTest {
    private CertificateDAOImpl certificateDaoMock = Mockito.mock(CertificateDAOImpl.class);
    private EntityValidator<Certificate> validator;
    private CertificateFinder finder = new CertificateFinder();
    private CertificateServiceImpl service;
    private TagServiceImpl tagServiceMock = Mockito.mock(TagServiceImpl.class);

    private Tag tag1 = new Tag("Tag1");
    private Certificate certificate1 = new Certificate("Certificate1",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private Certificate certificate2 = new Certificate("Certificate2",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private Certificate certificate3 = new Certificate("Certificate3",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private Certificate certificate4 = new Certificate("Certificate4",
            null, BigDecimal.valueOf(10.0), 3, null, null);
    private Certificate[] certificates = {certificate1, certificate2, certificate3, certificate4};
    private Certificate[] certificatesShort = {certificate1, certificate2};
    private List<Certificate> fullList = Arrays.asList(certificates);
    private List<Certificate> shortList = Arrays.asList(certificatesShort);

    {
        try {
            Mockito.when(tagServiceMock.read(Mockito.any(Integer.class))).thenReturn(tag1);
            Mockito.when(tagServiceMock.create(Mockito.any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgumentAt(0, Tag.class);
                tag.setId(tag.getId() + 1);
                return tag;
            });

            Mockito.when(certificateDaoMock.findAll()).thenReturn(fullList);
            Mockito.when(certificateDaoMock.read(1)).thenReturn(certificate1);
            Mockito.when(certificateDaoMock.read(2)).thenReturn(certificate2);
            Mockito.when(certificateDaoMock.findBy(Mockito.any(CertificateFinder.class))).thenReturn(shortList);
            Mockito.doNothing().when(certificateDaoMock).delete(Mockito.any(Integer.class));
            Mockito.doNothing().when(certificateDaoMock).update(Mockito.any(Certificate.class));
            Mockito.when(certificateDaoMock.create(Mockito.any(Certificate.class))).thenAnswer(invocation -> {
                Certificate certificate = invocation.getArgumentAt(0, Certificate.class);
                certificate.setId(fullList.size());
                return certificate;
            });
            Mockito.when(certificateDaoMock.iStagCertificateTied(
                    Mockito.any(Integer.class), Mockito.any(Integer.class)))
                    .thenReturn(false);
            Mockito.when(certificateDaoMock.iStagCertificateTied(1, 3)).thenReturn(true);

            validator = Mockito.mock(CertificateValidator.class);
            Mockito.doNothing().when(validator).validate(Mockito.any(Certificate.class));
        } catch (DAOSQLException | ValidationException | ServiceException e) {
            e.printStackTrace();
        }
        certificate1.setId(1);
        certificate2.setId(2);
        certificate1.setId(3);
        certificate2.setId(4);
        service = new CertificateServiceImpl(certificateDaoMock, validator, finder, tagServiceMock);
    }

    @Test
    public void readTest() throws ServiceException {
        assertEquals(certificate1, service.read(1));
    }

    @Test
    public void findAllTest() throws ServiceException {
        assertEquals(fullList, service.findAll());
    }

    @Test
    public void createTest() throws ServiceException, ValidationException, DAOSQLException {
        Certificate certificate = service.create(certificate1);
        assertEquals(certificate.getId(), fullList.size());
        Mockito.verify(certificateDaoMock, Mockito.times(1)).create(certificate1);
    }

    @Test
    public void deleteTest() throws ServiceException, DAOSQLException {
        service.delete(1);
        Mockito.verify(certificateDaoMock, Mockito.times(1)).read(1);
    }

    @Test
    public void updateTest() throws ServiceException, ValidationException, DAOSQLException {
        service.update(certificate2);
        Mockito.verify(certificateDaoMock, Mockito.times(1)).update(certificate2);
    }

    @Test
    public void findByTagTest() throws ServiceException, DAOSQLException {
        assertEquals(shortList, service.findByTag(1));
        CertificateFinder finder = new CertificateFinder();
        finder.findByTag(1);
        Mockito.verify(certificateDaoMock, Mockito.times(1)).findBy(finder);
    }

    @Test
    public void findTest() throws ServiceException, DAOSQLException {
        Map<String, String> params = new HashMap<>();
        params.put(CertificateSearchParameters.NAME.name(), "1");
        params.put(CertificateSortingParameters.SORT_BY_NAME.name().toLowerCase(), "2");

        assertEquals(shortList, service.find(params));
        Mockito.verify(certificateDaoMock, Mockito.times(1)).findBy(Mockito.any(CertificateFinder.class));
    }

    @Test
    public void addCertificateTagTest() throws ServiceException, ValidationException {
        service.addCertificateTag(1, tag1);
        Mockito.verify(certificateDaoMock, Mockito.times(1))
                .addCertificateTag(1, tag1.getId());
    }

    @Test
    public void addCertificateTag2Test() throws ServiceException, ValidationException {
        service.addCertificateTag(certificate1, 1);
        Mockito.verify(certificateDaoMock, Mockito.times(1))
                .addCertificateTag(certificate1.getId(), 1);
    }

    @Test
    public void deleteCertificateTagTest() throws ServiceException, ValidationException {
        service.deleteCertificateTag(1, 3);
        Mockito.verify(certificateDaoMock, Mockito.times(1))
                .deleteCertificateTag(1, 3);
    }
}
