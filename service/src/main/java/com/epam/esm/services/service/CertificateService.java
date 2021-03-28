package com.epam.esm.services.service;

import com.epam.esm.persistence.dao.AscDesc;
import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.dao.certificate.CertificateFinder;
import com.epam.esm.persistence.dao.certificate.MySQLCertificateDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CertificateService extends AbstractEntityService<Certificate> {
    
    private static CertificateService INSTANCE = new CertificateService();
    private static final String WRONG_DAO_MESSAGE = "Wrong DAO!";
    private EntityDAO<Certificate> dao;
    private EntityValidator<Certificate> validator;

    private CertificateService() {
    }

    @Autowired
    public void setDao(EntityDAO<Certificate> dao) {
        this.dao = dao;
    }
    @Autowired
    public void setValidator(EntityValidator<Certificate> validator) {
        this.validator = validator;
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
        if (dao instanceof MySQLCertificateDAO) {
            try {
                return ((MySQLCertificateDAO)dao).findBy(entityFinder);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

    public Collection<Certificate> findAll(Tag tag) throws ServiceException {
        try {
            if (dao instanceof MySQLCertificateDAO) {
                return ((MySQLCertificateDAO)dao).findAllByTag(tag);
            } else {
                throw new ServiceException(WRONG_DAO_MESSAGE);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public void addTagCertificate(Tag tag, Certificate certificate) throws ServiceException {
        if (dao instanceof MySQLCertificateDAO) {
            ((MySQLCertificateDAO)dao).addCertificateTag(certificate, tag);
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

    public void deleteTagCertificate(Tag tag, Certificate certificate) throws ServiceException {
        if (dao instanceof MySQLCertificateDAO) {
            ((MySQLCertificateDAO)dao).deleteCertificateTag(certificate, tag);
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

    public Collection<Certificate> find(String tagName, String name, String description, Map<String, String> sorting) throws ServiceException {
        if (dao instanceof MySQLCertificateDAO) {
            CertificateFinder finder = new CertificateFinder();
            if (StringUtils.isNotEmpty(name)) {
                finder = finder.findByName(name);
            }
            if (StringUtils.isNotEmpty(description)) {
                finder = finder.findByName(description);
            }
            if (StringUtils.isNotEmpty(tagName)) {
                finder = finder.findByName(name);
            }
            if (sorting.containsKey(sorting.get("sort-by-create"))) {
                finder = finder.sortByCreateDate(getValue(sorting.get("sort-by-create")));
            }
            if (sorting.containsKey(sorting.get("sort-by-lastupdate"))) {
                finder = finder.sortByLastUpdateDate(getValue(sorting.get("sort-by-lastupdate")));
            }
            if (sorting.containsKey(sorting.get("sort-by-name"))) {
                finder = finder.sortByName(getValue(sorting.get("sort-by-name")));
            }
            return findBy(finder);
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

    private AscDesc getValue(String s) {
        try {
            AscDesc.valueOf(s);
        } catch (IllegalArgumentException e) {
            return AscDesc.ASC;
        }
        return AscDesc.valueOf(s);
    }
}
