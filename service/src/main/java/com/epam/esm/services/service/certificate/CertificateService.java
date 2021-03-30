package com.epam.esm.services.service.certificate;

import com.epam.esm.model.entity.Entity;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.CertificateFinder;
import com.epam.esm.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.EntityService;
import com.epam.esm.services.service.tag.TagService;
import com.epam.esm.services.util.DoubleUtil;
import com.epam.esm.services.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateService implements EntityService<Certificate> {

    @Autowired
    private static CertificateService INSTANCE;

    private CertificateDAO dao;
    private EntityValidator<Certificate> validator;
    private CertificateFinder finder;
    private TagService tagService;

    @Autowired
    private CertificateService(CertificateDAO dao,
                               EntityValidator<Certificate> validator,
                               CertificateFinder finder, TagService tagService) {
        this.dao = dao;
        this.validator = validator;
        this.finder = finder;
        this.tagService = tagService;
    }

    public static EntityService<Certificate> getInstance() {
        return INSTANCE;
    }

    @Override
    public Certificate create(Certificate certificate) throws ServiceException, ValidationException {
        try {
            validator.validate(certificate);
            return dao.create(certificate);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Certificate read(int id) throws ServiceException {
        try {
            return dao.read(id);
        } catch (DAOException e) {
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
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Certificate certificate) throws ServiceException, ValidationException {
        try {
            validator.validate(certificate);
            dao.update(certificate);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Collection<Certificate> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Collection<Certificate> findBy(EntityFinder<Certificate> entityFinder) throws ServiceException {
            try {
                return  dao.findBy(entityFinder);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
    }

    public Collection<Certificate> find(Map<String, String> params) throws ServiceException {
        finder.newFinder();
        for (String key : params.keySet()) {
            if (key.contains("sort")) {
                finder.sortBy(CertificateSortingParameters.getEntryByParameter(key).getValue(),
                        AscDesc.getValue(params.get(key)));
            } else {
                Optional<CertificateSearchParameters> optional =
                Optional.ofNullable(CertificateSearchParameters.getEntryByParameter(key));
                if (optional.isPresent()) {
                    switch (optional.get()) {
                        case NAME:
                            finder.findByName(params.get(key));
                            break;
                        case DESCRIPTION:
                            if(StringUtils.isNotEmpty(params.get(key))) {
                                finder.findByDescription(params.get(key));
                            }
                            break;
                        case TAGNAME:
                            if(StringUtils.isNotEmpty(params.get(key))) {
                                finder.findByTag(params.get(key));
                            }
                            break;
                        case PRICE_LESS:
                            Optional<BigDecimal> optionalDouble = DoubleUtil.parseDoubleOptional(params.get(key));
                            optionalDouble.ifPresent(finder::findByPriceLess);
                            break;
                        case PRICE_MORE:
                            optionalDouble = DoubleUtil.parseDoubleOptional(params.get(key));
                            optionalDouble.ifPresent(finder::findByPriceMore);
                            break;
                    }
                }
            }
        }
        return findBy(finder);
    }

    public Collection<Certificate> findByTag(int tagId) throws ServiceException {
        finder.newFinder();
        finder.findByTag(tagId);
        return findBy(finder);
    }

    public void addCertificateTag(Certificate certificate, int tagId) throws ServiceException, ValidationException {
        update(certificate);
        dao.addCertificateTag(certificate.getId(), tagId);
    }

    public void addCertificateTag(int certificateId, Tag tag) throws ServiceException, ValidationException {
        tagService.update(tag);
        dao.addCertificateTag(certificateId, tag.getId());
    }

    public void deleteCertificateTag(int certificateId, int tagId) throws ServiceException, ValidationException {
            if (findByTag(tagId).stream().map(Entity::getId).anyMatch(id -> id == tagId)) {
                dao.deleteCertificateTag(certificateId, tagId);
            } else {
                throw new ServiceException("Entity does not exist!");
            }
    }
}


