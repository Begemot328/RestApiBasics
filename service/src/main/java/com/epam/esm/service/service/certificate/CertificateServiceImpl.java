package com.epam.esm.service.service.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.util.finder.impl.CertificateFinder;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PageableParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.tag.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link Certificate} service class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String NOT_FOUND_ERROR_MESSAGE = "Requested certificate not found(%s = %s)!";
    private final CertificateDAO dao;
    private final EntityValidator<Certificate> validator;
    private final TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDAO dao,
                                  EntityValidator<Certificate> validator,
                                  TagService tagService) {
        this.dao = dao;
        this.validator = validator;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public Certificate create(Certificate certificate)
            throws ValidationException, BadRequestException {

        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        validator.validate(certificate);
        tagService.makeAllTagsDetached(certificate.getTags());
        try {
            return dao.save(certificate);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }

    @Override
    public Certificate getById(int id) throws NotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, "id", id),
                        ErrorCodes.CERTIFICATE_NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(int id) throws BadRequestException {
        Optional<Certificate> certificate = dao.findById(id);
        if (certificate.isEmpty()) {
            throw new BadRequestException("Entity does not exist", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        dao.delete(certificate.get());
    }

    @Transactional
    @Override
    public Certificate update(Certificate certificate)
            throws ValidationException, NotFoundException, BadRequestException {
        Certificate oldCertificate = getById(certificate.getId());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setCreateDate(oldCertificate.getCreateDate());
        validator.validate(certificate);
        tagService.makeAllTagsDetached(certificate.getTags());
        return dao.save(certificate);
    }

    private List<Certificate> findByFinder(CertificateFinder entityFinder, Pageable pageable)
            throws NotFoundException {
        List<Certificate> certificates = dao.findAll(
                entityFinder.getPredicate(), pageable).getContent();
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("Requested certificate not found!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
    }

    @Lookup
    @Override
    public CertificateFinder getFinder() {
        return null;
    }

    @Override
    public List<Certificate> findByParameters(MultiValueMap<String, String> params, Pageable pageable)
            throws BadRequestException, NotFoundException {
        CertificateFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                if (!PageableParameters.contains(entry.getKey())) {
                    parseFindParameter(finder, entry.getKey(), entry.getValue());
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
            }
        }
        return findByFinder(finder, pageable);
    }

    private void parseFindParameter(CertificateFinder finder,
                                    String parameterName, List<String> parameterValues) {
        CertificateSearchParameters parameter =
                CertificateSearchParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case NAME:
                finder.findByNameLike(decodeParam(parameterValues.get(0)));
                break;
            case DESCRIPTION:
                finder.findByDescriptionLike(decodeParam(parameterValues.get(0)));
                break;
            case TAG_ID:
                finder.findByTags(parameterValues.stream().mapToInt(Integer::parseInt).toArray());
                break;
            case TAG_NAME:
                finder.findByTags(parameterValues.toArray(String[]::new));
                break;
        }
    }

    @Transactional
    @Override
    public Certificate patch(Certificate certificate)
            throws ValidationException, BadRequestException, NotFoundException {
        return update(applyChanges(certificate, getById(certificate.getId())));
    }

    private Certificate applyChanges(Certificate newCertificate, Certificate oldCertificate) {
        if (newCertificate.getPrice() != null
                && newCertificate.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            oldCertificate.setPrice(newCertificate.getPrice());
        }
        if (newCertificate.getDescription() != null) {
            oldCertificate.setDescription(newCertificate.getDescription());
        }
        if (newCertificate.getDuration() != 0) {
            oldCertificate.setDuration(newCertificate.getDuration());
        }
        if (newCertificate.getName() != null) {
            oldCertificate.setName(newCertificate.getName());
        }
        if (CollectionUtils.isNotEmpty(newCertificate.getTags())) {
            oldCertificate.setTags(newCertificate.getTags());
        }
        return oldCertificate;
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }
}


