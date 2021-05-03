package com.epam.esm.service.service.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.SortDirection;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TagDAO tagDAO;

    @Autowired
    public CertificateServiceImpl(CertificateDAO dao,
                                  EntityValidator<Certificate> validator,
                                  TagDAO tagDAO) {
        this.dao = dao;
        this.validator = validator;
        this.tagDAO = tagDAO;
    }

    @Override
    public Certificate create(Certificate certificate) throws ServiceException, ValidationException {
        try {
            certificate.setCreateDate(LocalDateTime.now());
            certificate.setLastUpdateDate(LocalDateTime.now());
            validator.validate(certificate);
            return dao.create(certificate);
        } catch (DAOSQLException e) {
            throw new ServiceException(e, ErrorCodes.CERTIFICATE_INTERNAL_ERROR);
        }
    }

    @Override
    public Certificate read(int id) throws NotFoundException {
        Certificate certificate = dao.read(id);
        if (certificate == null) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificate;
        }
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
    public Certificate update(Certificate certificate) throws ValidationException {
        certificate.setLastUpdateDate(LocalDateTime.now());
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
            case TAGNAME:
                addToFinder(finder::findByTag, list);
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
                    finder.findByTags(list.stream().mapToInt(Integer::parseInt).toArray());
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

    @Override
    public List<Certificate> read(MultiValueMap<String, String> params) throws BadRequestException, NotFoundException {
        CertificateFinder finder = new CertificateFinder();
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
    public Certificate patch(Certificate certificate) throws ValidationException, BadRequestException {
        if (certificate.getId() <= 0) {
            throw new BadRequestException("Wrong certificate id!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }

        Certificate oldCertificate = dao.read(certificate.getId());

        if (oldCertificate == null) {
            throw new BadRequestException("Wrong certificate id!", ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        if (certificate.getPrice() != null
                && certificate.getPrice().compareTo(BigDecimal.ZERO) > 0 ) {
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
        return update(oldCertificate);
    }

    @Transactional
    @Override
    public void addCertificateTag(Certificate certificate, int tagId) throws ServiceException,
            BadRequestException, ValidationException {
        Optional<Certificate> certificateOptional;
        if (dao.isTagCertificateTied(certificate.getId(), tagId)) {
            throw new BadRequestException("adding existing relation",
                    ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        certificateOptional = Optional.ofNullable(dao.read(certificate.getId()));
        if (certificateOptional.isEmpty()) {
            create(certificate);
        }
        dao.addCertificateTag(certificate.getId(), tagId);
    }

    @Transactional
    @Override
    public void addCertificateTag(int certificateId, Tag tag) throws ServiceException,
            BadRequestException {
        if (dao.isTagCertificateTied(certificateId, tag.getId())) {
            throw new BadRequestException("adding existing relation",
                    ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        Optional<Tag> tagOptional = Optional.ofNullable(tagDAO.read(tag.getId()));
        if (tagOptional.isEmpty()) {
            try {
                tagDAO.create(tag);
            } catch (DAOSQLException e) {
                throw new ServiceException(e, ErrorCodes.CERTIFICATE_INTERNAL_ERROR);
            }
        }
        dao.addCertificateTag(certificateId, tag.getId());
    }

    @Transactional
    @Override
    public void addCertificateTags(int certificateId, Tag[] tags) throws BadRequestException,
            ServiceException {
        for (Tag tag : tags) {
            addCertificateTag(certificateId, tag);
        }
    }

    @Transactional
    @Override
    public void addCertificatesTag(Certificate[] certificates, int tagId) throws BadRequestException,
            ServiceException, ValidationException {
        for (Certificate certificate : certificates) {
            addCertificateTag(certificate, tagId);
        }
    }

    @Transactional
    @Override
    public void deleteCertificateTag(int certificateId, int tagId) throws BadRequestException {
        if (dao.isTagCertificateTied(certificateId, tagId)) {
            dao.deleteCertificateTag(certificateId, tagId);
        } else {
            throw new BadRequestException("Entity does not exist!",
                    ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
    }
}


