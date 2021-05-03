package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.certificate.CertificateDAOImpl;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CertificateServiceImplTests {
    static Logger logger = LoggerFactory.getLogger(CertificateServiceImplTests.class);

    private static CertificateDAOImpl certificateDaoMock;
    private static EntityValidator<Certificate> validator;
    private static CertificateFinder finder = new CertificateFinder();
    private static CertificateServiceImpl service;

    private static Tag tag1 = new Tag("Tag1");
    private static Certificate certificate1 = new Certificate("Certificate1",
            BigDecimal.valueOf(10.0), 3);
    private static Certificate certificate2 = new Certificate("Certificate2",
            BigDecimal.valueOf(10.0), 3);
    private static Certificate certificate3 = new Certificate("Certificate3",
            BigDecimal.valueOf(10.0), 3);
    private static Certificate certificate4 = new Certificate("Certificate4",
            BigDecimal.valueOf(10.0), 3);
    private static Certificate[] certificates = {certificate1, certificate2, certificate3, certificate4};
    private static Certificate[] certificatesShort = {certificate1, certificate2};
    private static List<Certificate> fullList = Arrays.asList(certificates);
    private static List<Certificate> shortList = Arrays.asList(certificatesShort);


    @BeforeEach
    void init() {
        certificateDaoMock = mock(CertificateDAOImpl.class);
        TagDAOImpl tagDAOMock = mock(TagDAOImpl.class);
        try {
            when(tagDAOMock.read(any(Integer.class))).thenReturn(tag1);
            when(tagDAOMock.create(any(Tag.class))).thenAnswer(invocation -> {
                Tag tag = invocation.getArgument(0, Tag.class);
                tag.setId(tag.getId() + 1);
                return tag;
            });
            when(certificateDaoMock.readAll()).thenReturn(fullList);
            when(certificateDaoMock.read(1)).thenReturn(certificate1);
            when(certificateDaoMock.read(2)).thenReturn(certificate2);
            when(certificateDaoMock.readBy(any(CertificateFinder.class))).thenReturn(shortList);
            doNothing().when(certificateDaoMock).delete(any(Integer.class));
            when(certificateDaoMock.update(any(Certificate.class))).thenAnswer(invocation -> invocation.getArgument(0, Certificate.class));
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
        } catch (DAOSQLException | ValidationException e) {
            logger.error(e.getMessage());
        }
        certificate1.setId(1);
        certificate2.setId(2);
        certificate3.setId(3);
        certificate4.setId(4);
        service = new CertificateServiceImpl(certificateDaoMock, validator, tagDAOMock);
    }

    @Test
    public void read_returnCertificate() throws NotFoundException {
        assertEquals(certificate1, service.read(1));
    }

    @Test
    public void readAll_returnCertificates() throws NotFoundException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    public void create_createCertificate() throws ServiceException, DAOSQLException, ValidationException {
        Certificate certificate = service.create(certificate1);
        assertEquals(certificate.getId(), fullList.size());
        verify(certificateDaoMock, times(1)).create(certificate1);
    }

    @Test
    public void delete_deleteCertificate() throws BadRequestException {
        service.delete(1);
        verify(certificateDaoMock, times(1)).read(1);
    }

    @Test
    public void update_updateCertificate() throws ValidationException {
        service.update(certificate2);
        verify(certificateDaoMock, times(1)).update(certificate2);
    }

    @Test
    public void read_returnTags() throws BadRequestException, NotFoundException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {
        };
        params.put(CertificateSearchParameters.NAME.name(), Collections.singletonList("1"));
        params.put(CertificateSortingParameters.SORT_BY_NAME.name().toLowerCase(),
                Collections.singletonList("2"));
        assertEquals(shortList, service.read(params));
        verify(certificateDaoMock, times(1)).readBy(any(CertificateFinder.class));
    }

    @Test
    public void addCertificateTag_passTag_addTie() throws ServiceException, BadRequestException {
        service.addCertificateTag(1, tag1);
        verify(certificateDaoMock, times(1))
                .addCertificateTag(1, tag1.getId());
    }

    @Test
    public void addCertificateTags_passCertificates_addTies()
            throws ServiceException, BadRequestException {
        when(certificateDaoMock.isTagCertificateTied(any(Integer.class), any(Integer.class)))
                .thenReturn(false);
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag[] tags = {tag1, tag2};
        service.addCertificateTags(1, tags);

        verify(certificateDaoMock, atLeast(shortList.size()))
                .addCertificateTag(eq(1), any(Integer.class));
    }

    @Test
    public void addCertificatesTag_passCertificates_addTies()
            throws ServiceException, BadRequestException, ValidationException {
        when(certificateDaoMock.isTagCertificateTied(any(Integer.class), any(Integer.class)))
                .thenReturn(false);
        service.addCertificatesTag(shortList.toArray(Certificate[]::new), 1);

        verify(certificateDaoMock, atLeast(shortList.size()))
                .addCertificateTag(any(Integer.class), eq(1));
    }

    @Test
    public void addCertificateTag_passCertificate_addTie()
            throws ServiceException, BadRequestException, ValidationException {
        service.addCertificateTag(certificate1, 1);
        verify(certificateDaoMock, times(1))
                .addCertificateTag(eq(certificate1.getId()), eq(1));
    }

    @Test
    public void deleteCertificateTag_deleteTie() throws BadRequestException {
        service.deleteCertificateTag(1, 3);
        verify(certificateDaoMock, times(1))
                .deleteCertificateTag(1, 3);
    }

    @Test
    public void create_invalidCertificate_throwValidationException()
            throws ValidationException {
        doThrow(new ValidationException("error", ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION))
                .when(validator).validate(any(Certificate.class));
        assertThrows(ValidationException.class, () -> service.create(certificate1));
    }

    @Test
    public void create_catchSQLException_throwsException()
            throws DAOSQLException {
        when(certificateDaoMock.create(any(Certificate.class))).thenThrow(new DAOSQLException("error"));
        assertThrows(ServiceException.class, () -> service.create(certificate1));
    }

    @Test
    public void patch_changeName_updateCertificate() throws BadRequestException, ValidationException {
        Certificate certificate = new Certificate("new name", null, 0);
        certificate.setId(certificate2.getId());
        certificate2.setName(certificate.getName());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).update(certificate2);
    }

    @Test
    public void patch_changeDuration_updateCertificate() throws BadRequestException, ValidationException {
        Certificate certificate = new Certificate(null, null, 5);
        certificate.setId(certificate2.getId());
        certificate2.setDuration(certificate.getDuration());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).update(certificate2);
    }

    @Test
    public void patch_changeDescription_updateCertificate() throws BadRequestException, ValidationException {
        Certificate certificate = new Certificate(null, null, 0);
        certificate.setDescription("description");
        certificate.setId(certificate2.getId());
        certificate2.setDescription(certificate.getDescription());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).update(certificate2);
    }

    @Test
    public void patch_wrongId_throwsException() {
        Certificate certificate = new Certificate(null, null, 5);
        certificate.setId(10);
        certificate2.setDuration(certificate.getDuration());
        assertThrows(BadRequestException.class, () -> service.patch(certificate));
    }
}
