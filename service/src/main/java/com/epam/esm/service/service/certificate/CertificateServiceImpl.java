package com.epam.esm.service.service.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.SortDirection;
import com.epam.esm.persistence.util.finder.impl.CertificateFinder;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.tag.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@link Certificate} service class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class CertificateServiceImpl implements CertificateService {

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
        makeAllTagsDetached(certificate);
        try {
            return dao.save(certificate);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public Optional<Certificate> getById(int id) {
        return dao.findById(id);
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
        Certificate oldCertificate = findOldCertificate(certificate.getId());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setCreateDate(oldCertificate.getCreateDate());
        validator.validate(certificate);
        makeAllTagsDetached(certificate);
        return dao.save(certificate);
    }

    @Override
    public List<Certificate> findAll() throws NotFoundException {
        List<Certificate> certificates = IterableUtils.toList(dao.findAll());
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("No certificates found!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
    }

    private List<Certificate> findByFinder(EntityFinder<Certificate> entityFinder) throws NotFoundException {
        List<Certificate> certificates = dao.findByParameters(entityFinder);
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
    public List<Certificate> findByParameters(MultiValueMap<String, String> params)
            throws BadRequestException, NotFoundException {
        CertificateFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                parseParameter(finder, entry.getKey(), entry.getValue());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
            }
        }
        return findByFinder(finder);
    }

    @Transactional
    @Override
    public Certificate patch(Certificate certificate)
            throws ValidationException, BadRequestException, NotFoundException {
        Certificate oldCertificate = findOldCertificate(certificate.getId());
        return update(applyChanges(certificate, oldCertificate));
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

    private void makeAllTagsDetached(Certificate certificate)
            throws BadRequestException, ValidationException {
        for (Tag tag : certificate.getTags()) {
            if (tagService.getById(tag.getId()).isEmpty()) {
                Optional<Tag> tagOptional = tagService.getByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tag.setId(tagOptional.get().getId());
                } else {
                    tagService.create(tag);
                }
            }
        }
    }

    private void parseParameter(CertificateFinder finder,
                                String parameterName,
                                List<String> parameterValues) {
        if (parameterName.contains("sort")) {
            addSortingToFinder(finder, getSortingParameter(parameterName),
                    getSortDirection(parameterValues));
        } else if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, parameterValues);
        } else {
            parseFindParameter(finder, parameterName, parameterValues);
        }
    }

    private void parsePaginationParameter(CertificateFinder finder,
                                          String parameterName,
                                          List<String> parameterValues) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(parameterValues.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(parameterValues.get(0)));
                break;
        }
    }

    private void addSortingToFinder(CertificateFinder finder,
                                    String sortingParameter,
                                    SortDirection sortDirection) {
        finder.sortBy(sortingParameter, sortDirection);
    }

    private String getSortingParameter(String parameterName) {
        return CertificateSortingParameters.getEntryByParameter(parameterName).getValue();
    }

    private SortDirection getSortDirection(List<String> parameterValues) {
        return SortDirection.parseAscDesc(parameterValues.get(0));
    }

    private void parseFindParameter(CertificateFinder finder,
                                    String parameterName, List<String> parameterValues) {
        CertificateSearchParameters parameter =
                CertificateSearchParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case NAME:
                addToFinder(finder::findByName, parameterValues);
                break;
            case DESCRIPTION:
                addToFinder(finder::findByDescription, parameterValues);
                break;
            case TAG_ID:
                if (parameterValues.size() > 1) {
                    finder.findByTags(parameterValues.stream().mapToInt(Integer::parseInt).toArray());
                } else {
                    finder.findByTagId(Integer.parseInt(parameterValues.get(0)));
                }
                break;
            case TAG_NAME:
                if (parameterValues.size() > 1) {
                    finder.findByTags(parameterValues.toArray(String[]::new));
                } else {
                    finder.findByTag(parameterValues.get(0));
                }
                break;
        }
    }

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }

    private void addToFinder(Consumer<String> consumer, List<String> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            addToFinder(consumer, list.get(0));
        }
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }

    private Certificate findOldCertificate(int certificateId) throws NotFoundException {
        return dao.findById(certificateId).orElseThrow(
                () -> new NotFoundException(String.format(notFoundErrorMessage, "id",
                        certificateId),
                        ErrorCodes.CERTIFICATE_NOT_FOUND));
    }
}


