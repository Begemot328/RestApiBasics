package test.com.epam.esm.service.service;

import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.validator.EntityValidator;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
class CertificateServiceImplTests {

    static final Logger logger = LoggerFactory.getLogger(CertificateServiceImplTests.class);
    private static final Tag tag1 = new Tag("Tag1");
    private static final Certificate certificate1 = new Certificate("Certificate1",
            BigDecimal.valueOf(10.0), 3);
    private static final Certificate certificate2 = new Certificate("Certificate2",
            BigDecimal.valueOf(10.0), 3);
    private static final Certificate certificate3 = new Certificate("Certificate3",
            BigDecimal.valueOf(10.0), 3);
    private static final Certificate certificate4 = new Certificate("Certificate4",
            BigDecimal.valueOf(10.0), 3);
    private static final Certificate[] certificates = {certificate1, certificate2, certificate3, certificate4};
    private static final Certificate[] certificatesShort = {certificate1, certificate2};
    private static final List<Certificate> fullList = Arrays.asList(certificates);
    private static final List<Certificate> shortList = Arrays.asList(certificatesShort);

    @MockBean
    CertificateDAO certificateDaoMock;

    @MockBean
    TagDAO tagDaoMock;

    @MockBean
    EntityValidator<Certificate> validator;

    @Autowired
    CertificateService service;

    @BeforeEach
    void init() throws ValidationException {

        when(tagDaoMock.findById(any(Integer.class))).thenReturn(Optional.of(tag1));
        when(tagDaoMock.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0, Tag.class);
            tag.setId(tag.getId() + 1);
            return tag;
        });
        when(certificateDaoMock.findAll()).thenReturn(fullList);
        when(certificateDaoMock.findById(1)).thenReturn(Optional.of(certificate1));
        when(certificateDaoMock.findById(2)).thenReturn(Optional.of(certificate2));
        when(certificateDaoMock.findAll(any(BooleanExpression.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(shortList));
        doNothing().when(certificateDaoMock).delete(any(Certificate.class));
        when(certificateDaoMock.save(any(Certificate.class))).thenAnswer(
                invocation -> invocation.getArgument(0, Certificate.class));
        when(certificateDaoMock.save(any(Certificate.class))).thenAnswer(invocation -> {
            Certificate certificate = invocation.getArgument(0, Certificate.class);
            certificate.setId(fullList.size());
            return certificate;
        });
        doNothing().when(validator).validate(any(Certificate.class));

        certificate1.setId(1);
        certificate2.setId(2);
        certificate3.setId(3);
        certificate4.setId(4);
    }

    @Test
    void getById_returnCertificate() throws NotFoundException {
        assertEquals(certificate1, service.getById(1));
    }

    @Test
    void getById_invalidId_throwException() {
        assertThrows(NotFoundException.class, () -> service.getById(1000));
    }

    @Test
    void create_createCertificate() throws ValidationException, BadRequestException {
        Certificate certificate = service.create(certificate1);
        assertEquals(certificate.getId(), fullList.size());
        verify(certificateDaoMock, times(1)).save(certificate1);
    }

    @Test
    void delete_deleteCertificate() throws BadRequestException {
        service.delete(1);
        verify(certificateDaoMock, times(1)).findById(1);
    }

    @Test
    void update_updateCertificate() throws ValidationException, NotFoundException, BadRequestException {
        service.update(certificate2);
        verify(certificateDaoMock, times(1)).save(certificate2);
    }

    @Test
    void findByParameters_returnTags() throws BadRequestException, NotFoundException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(CertificateSearchParameters.NAME.name(), Collections.singletonList("1"));
        assertEquals(shortList, service.findByParameters(params, Pageable.unpaged()));
        verify(certificateDaoMock, times(1))
                .findAll(any(BooleanExpression.class), any(Pageable.class));
    }

    @Test
    void save_invalidCertificate_throwValidationException()
            throws ValidationException {
        doThrow(new ValidationException("error", ErrorCodes.CERTIFICATE_VALIDATION_EXCEPTION))
                .when(validator).validate(any(Certificate.class));
        assertThrows(ValidationException.class, () -> service.create(certificate1));
    }

    @Test
    void save_catchDataAccessException_throwsException() {
        doThrow(new DataIntegrityViolationException("error"))
                .when(certificateDaoMock).save(any(Certificate.class));
        assertThrows(BadRequestException.class, () -> service.create(certificate1));
    }

    @Test
    void patch_changeName_updateCertificate()
            throws BadRequestException, ValidationException, NotFoundException {
        Certificate certificate = new Certificate("new name", null, 0);
        certificate.setId(certificate2.getId());
        certificate2.setName(certificate.getName());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).save(certificate2);
    }

    @Test
    void patch_changeDuration_updateCertificate()
            throws BadRequestException, ValidationException, NotFoundException {
        Certificate certificate = new Certificate(null, null, 5);
        certificate.setId(certificate2.getId());
        certificate2.setDuration(certificate.getDuration());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).save(certificate2);
    }

    @Test
    void patch_changeDescription_updateCertificate()
            throws BadRequestException, ValidationException, NotFoundException {
        Certificate certificate = new Certificate(null, null, 0);
        certificate.setDescription("description");
        certificate.setId(certificate2.getId());
        certificate2.setDescription(certificate.getDescription());
        service.patch(certificate);
        verify(certificateDaoMock, atLeast(1)).save(certificate2);
    }

    @Test
    void patch_wrongId_throwsException() {
        Certificate certificate = new Certificate(null, null, 5);
        certificate.setId(10);
        certificate2.setDuration(certificate.getDuration());
        assertThrows(NotFoundException.class, () -> service.patch(certificate));
    }
}
