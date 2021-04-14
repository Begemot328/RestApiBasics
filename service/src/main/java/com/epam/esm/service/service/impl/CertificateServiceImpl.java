package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.SortDirection;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.service.CertificateService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    private CertificateDAO dao;
    private EntityValidator<Certificate> validator;
    private TagDAO tagDAO;

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

    @Override
    public List<Certificate> readBy(EntityFinder<Certificate> entityFinder) throws NotFoundException {
        List<Certificate> certificates = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("Requested certificate not found!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
    }

    private void parseFindParameter(CertificateFinder finder, String parameterString, String value) {
        CertificateSearchParameters parameter =
                CertificateSearchParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case NAME:
                addToFinder(finder::findByName, value);
                break;
            case DESCRIPTION:
                addToFinder(finder::findByDescription, value);
                break;
            case TAGNAME:
                addToFinder(finder::findByTag, value);
                break;
        }
    }

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }

    @Override
    public List<Certificate> read(Map<String, String> params) throws BadRequestException, NotFoundException {
        CertificateFinder finder = new CertificateFinder();
        for (String key : params.keySet()) {
            try {
                if (key.contains("sort")) {
                    finder.sortBy(CertificateSortingParameters.getEntryByParameter(key).getValue(),
                            SortDirection.parseAscDesc(params.get(key)));
                } else {
                    parseFindParameter(finder, key, params.get(key));
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    @Override
    public List<Certificate> readByTag(int tagId) throws NotFoundException {
        CertificateFinder finder = new CertificateFinder();
        finder.findByTag(tagId);
        List<Certificate> certificates = readBy(finder);
        if (CollectionUtils.isEmpty(certificates)) {
            throw new NotFoundException("Requested resource not found (Tag id = " + tagId + ")!",
                    ErrorCodes.CERTIFICATE_NOT_FOUND);
        } else {
            return certificates;
        }
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
        Optional<Tag> tagOptional;
        if (dao.isTagCertificateTied(certificateId, tag.getId())) {
            throw new BadRequestException("adding existing relation",
                    ErrorCodes.CERTIFICATE_BAD_REQUEST);
        }
        tagOptional = Optional.ofNullable(tagDAO.read(tag.getId()));
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


