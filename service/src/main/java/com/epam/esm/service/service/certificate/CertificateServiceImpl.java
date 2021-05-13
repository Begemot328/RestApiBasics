package com.epam.esm.service.service.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.SortDirection;
import com.epam.esm.persistence.util.finder.impl.CertificateFinder;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
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
        for (Tag tag : certificate.getTags()) {
            if (tagService.readOptional(tag.getId()).isEmpty()) {
                tagService.create(tag);
            }
        }
        try {
        return dao.create(certificate);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }

    @Override
    public Certificate read(int id) throws NotFoundException {
        Optional<Certificate> certificateOptional = readOptional(id);
        if (certificateOptional.isEmpty()) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificateOptional.get();
        }
    }

    @Override
    public Optional<Certificate> readOptional(int id) {
        return Optional.ofNullable(dao.read(id));
    }

    @Transactional
    @Override
    public void delete(int id) throws BadRequestException {
        if (dao.read(id) == null) {
            throw new BadRequestException("Entity does not exist", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        dao.delete(id);
    }

    @Transactional
    @Override
    public Certificate update(Certificate certificate)
            throws ValidationException, NotFoundException {
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setCreateDate(read(certificate.getId()).getCreateDate());
        validator.validate(certificate);
        return dao.update(certificate);

    }

    @Override
    public List<Certificate> readAll() throws NotFoundException {
        List<Certificate> certificates = dao.readAll();
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("No certificates found!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
    }

    private List<Certificate> readBy(EntityFinder<Certificate> entityFinder) throws NotFoundException {
        List<Certificate> certificates = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("Requested certificate not found!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
    }

    private void parsePaginationParameter(CertificateFinder finder, String parameterString, List<String> list) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(list.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(list.get(0)));
                break;
        }
    }

    private void parseFindParameter(CertificateFinder finder, String parameterString, List<String> list) {
        CertificateSearchParameters parameter =
                CertificateSearchParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case NAME:
                addToFinder(finder::findByName, list);
                break;
            case DESCRIPTION:
                addToFinder(finder::findByDescription, list);
                break;
            case TAG_ID:
                if (list.size() > 1) {
                    finder.findByTags(list.stream().mapToInt(Integer::parseInt).toArray());
                } else {
                    finder.findByTagId(Integer.parseInt(list.get(0)));
                }
                break;
            case TAG_NAME:
                if (list.size() > 1) {
                    finder.findByTags(list.toArray(String[]::new));
                } else {
                    finder.findByTag(list.get(0));
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

    @Lookup
    @Override
    public CertificateFinder getFinder() {
        return null;
    }

    @Override
    public List<Certificate> read(MultiValueMap<String, String> params)
            throws BadRequestException, NotFoundException {
        CertificateFinder finder = getFinder();
        for (String key : params.keySet()) {
            try {
                if (CollectionUtils.isEmpty(params.get(key))) {
                    throw new BadRequestException("Empty parameter!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
                }
                if (key.contains("sort")) {
                    finder.sortBy(CertificateSortingParameters.getEntryByParameter(key).getValue(),
                            SortDirection.parseAscDesc(params.get(key).get(0)));
                } else if (PaginationParameters.contains(key)) {
                    parsePaginationParameter(finder, key, params.get(key));
                } else {
                    parseFindParameter(finder, key, params.get(key));
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    @Transactional
    @Override
    public Certificate patch(Certificate certificate)
            throws ValidationException, BadRequestException, NotFoundException {
        if (certificate.getId() <= 0) {
            throw new BadRequestException("Wrong certificate id!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }

        Certificate oldCertificate = dao.read(certificate.getId());

        if (oldCertificate == null) {
            throw new BadRequestException("Wrong certificate id!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        if (certificate.getPrice() != null
                && certificate.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            oldCertificate.setPrice(certificate.getPrice());
        }
        if (certificate.getDescription() != null) {
            oldCertificate.setDescription(certificate.getDescription());
        }
        if (certificate.getDuration() != 0) {
            oldCertificate.setDuration(certificate.getDuration());
        }
        if (certificate.getName() != null) {
            oldCertificate.setName(certificate.getName());
        }
        if (CollectionUtils.isNotEmpty(certificate.getTags())) {
            oldCertificate.setTags(certificate.getTags());
        }
        return update(oldCertificate);
    }
}


