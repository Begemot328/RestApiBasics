package com.epam.esm.services.service.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.CertificateDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.persistence.dao.impl.CertificateDAOImpl;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.constants.CertificateSearchParameters;
import com.epam.esm.services.constants.CertificateSortingParameters;
import com.epam.esm.services.service.CertificateService;
import com.epam.esm.services.service.TagService;
import com.epam.esm.services.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    private CertificateDAO dao;
    private EntityValidator<Certificate> validator;
    private CertificateFinder finder;
    private TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDAO dao,
                                  EntityValidator<Certificate> validator,
                                  CertificateFinder finder, TagService tagService) {
        this.dao = dao;
        this.validator = validator;
        this.finder = finder;
        this.tagService = tagService;
    }

    @Override
    public Certificate create(Certificate certificate) throws ServiceException, ValidationException {
        try {
            validator.validate(certificate);
            return dao.create(certificate);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Certificate read(int id) throws ServiceException {
        try {
            return dao.read(id);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            if (dao.read(id) == null) {
                throw new ServiceException("Entity does not exist!");
            }
            dao.delete(id);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Certificate certificate) throws ServiceException, ValidationException {
        try {
            validator.validate(certificate);
            dao.update(certificate);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Certificate> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Certificate> findBy(EntityFinder<Certificate> entityFinder) throws ServiceException {
        try {
            return dao.findBy(entityFinder);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Certificate> find(Map<String, String> params) throws ServiceException {
        finder.newFinder();
        for (String key : params.keySet()) {
            try {
                if (key.contains("sort")) {
                    finder.sortBy(CertificateSortingParameters.getEntryByParameter(key).getValue(),
                            parseAscDesc(params.get(key)));
                } else {

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
                                finder.findByTagName(URLDecoder.decode(params.get(key), StandardCharsets.UTF_8));
                            }
                            break;
                        case PRICE_LESS:
                            try {
                                finder.findByPriceLess(new BigDecimal(params.get(key)));
                            } catch (NumberFormatException e) {
                                throw new BadRequestException(e);
                            }
                            break;
                        case PRICE_MORE:
                            try {
                                finder.findByPriceMore(new BigDecimal(params.get(key)));
                            } catch (NumberFormatException e) {
                                throw new BadRequestException(e);
                            }
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e);
            }
        }
        return findBy(finder);
    }

    @Override
    public List<Certificate> findByTag(int tagId) throws ServiceException {
        finder.newFinder();
        finder.findByTag(tagId);
        return findBy(finder);
    }

    @Override
    public void addCertificateTag(Certificate certificate, int tagId) throws ServiceException, ValidationException {
        Optional<Certificate> certificateOptional;
        if (dao.iStagCertificateTied(certificate.getId(), tagId)) {
            throw new BadRequestException("adding existing relation");
        }
        certificateOptional = Optional.ofNullable(read(certificate.getId()));
        if (certificateOptional.isEmpty()) {
            create(certificate);
        }
        dao.addCertificateTag(certificate.getId(), tagId);
    }

    @Override
    public void addCertificateTag(int certificateId, Tag tag) throws ServiceException, ValidationException {
        Optional<Tag> tagOptional;
        if (dao.iStagCertificateTied(certificateId, tag.getId())) {
            throw new BadRequestException("adding existing relation");
        }
        tagOptional = Optional.ofNullable(tagService.read(tag.getId()));
        if (tagOptional.isEmpty()) {
            tagService.create(tag);
        }
        dao.addCertificateTag(certificateId, tag.getId());
    }

    @Override
    public void deleteCertificateTag(int certificateId, int tagId) throws ServiceException {
        if (dao.iStagCertificateTied(certificateId, tagId)) {
            dao.deleteCertificateTag(certificateId, tagId);
        } else {
            throw new BadRequestException("Entity does not exist!");
        }
    }
}


