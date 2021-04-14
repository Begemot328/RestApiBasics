package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.CertificateSearchParameters;
import com.epam.esm.service.constants.CertificateSortingParameters;
import com.epam.esm.service.service.CertificateService;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    private CertificateDAO dao;
    private EntityValidator<Certificate> validator;
    private TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDAO dao,
                                  EntityValidator<Certificate> validator,
                                  TagService tagService) {
        this.dao = dao;
        this.validator = validator;
        this.tagService = tagService;
    }

    @Override
    public Certificate create(Certificate certificate) throws ServiceException {
        try {
            certificate.setCreateDate(LocalDateTime.now());
            certificate.setLastUpdateDate(LocalDateTime.now());
            validator.validate(certificate);
            return dao.create(certificate);
        } catch (DAOSQLException e) {
            throw new ServiceException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ValidationException e) {
            throw new ServiceException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public Certificate read(int id) {
            return dao.read(id);
    }

    @Transactional
    @Override
    public void delete(int id) throws ServiceException {
            if (dao.read(id) == null) {
                throw new ServiceException("Entity does not exist", ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            dao.delete(id);
    }

    @Transactional
    @Override
    public Certificate update(Certificate certificate) throws ServiceException {
        try {
            certificate.setLastUpdateDate(LocalDateTime.now());
            validator.validate(certificate);
            return dao.update(certificate);
        } catch (ValidationException e) {
            throw new ServiceException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public List<Certificate> readAll() {
            return dao.readAll();
    }

    @Override
    public List<Certificate> readBy(EntityFinder<Certificate> entityFinder) {
            return dao.readBy(entityFinder);
    }

    @Override
    public List<Certificate> read(Map<String, String> params) throws BadRequestException {
        CertificateFinder finder = new CertificateFinder();
        for (String key : params.keySet()) {
            try {
                if (key.contains("sort")) {
                    finder.sortBy(CertificateSortingParameters.getEntryByParameter(key).getValue(),
                            parseAscDesc(params.get(key)));
                } else if (StringUtils.isNotEmpty(key)) {
                    CertificateSearchParameters parameter = CertificateSearchParameters.getEntryByParameter(key);
                    switch (parameter) {
                        case NAME:
                            finder.findByName(URLDecoder.decode(params.get(key), StandardCharsets.UTF_8));
                            break;
                        case DESCRIPTION:
                            if (StringUtils.isNotEmpty(params.get(key))) {
                                finder.findByDescription(URLDecoder.decode(params.get(key), StandardCharsets.UTF_8));
                            }
                            break;
                        case TAGNAME:
                            if (StringUtils.isNotEmpty(params.get(key))) {
                                finder.findByTag(URLDecoder.decode(params.get(key), StandardCharsets.UTF_8));
                            }
                            break;
                        case PRICE_LESS:
                            try {
                                finder.findByPriceLess(new BigDecimal(params.get(key)));
                            } catch (NumberFormatException e) {
                                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX,
                                        HttpStatus.BAD_REQUEST);
                            }
                            break;
                        case PRICE_MORE:
                            try {
                                finder.findByPriceMore(new BigDecimal(params.get(key)));
                            } catch (NumberFormatException e) {
                                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX,
                                        HttpStatus.BAD_REQUEST);
                            }
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    @Override
    public List<Certificate> readByTag(int tagId) {
        CertificateFinder finder = new CertificateFinder();
        finder.findByTag(tagId);
        return readBy(finder);
    }

    @Transactional
    @Override
    public void addCertificateTag(Certificate certificate, int tagId) throws ServiceException,
            BadRequestException {
        Optional<Certificate> certificateOptional;
        if (dao.isTagCertificateTied(certificate.getId(), tagId)) {
            throw new BadRequestException("adding existing relation",
                    ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.BAD_REQUEST);
        }
        certificateOptional = Optional.ofNullable(read(certificate.getId()));
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
                    ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.BAD_REQUEST);
        }
        tagOptional = Optional.ofNullable(tagService.read(tag.getId()));
        if (tagOptional.isEmpty()) {
                tagService.create(tag);
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
            ServiceException {
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
                    ErrorCodes.CERTIFICATE_ERROR_CODE_SUFFIX, HttpStatus.BAD_REQUEST);
        }
    }
}


