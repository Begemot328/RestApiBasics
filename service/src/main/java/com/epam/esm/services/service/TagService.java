package com.epam.esm.services.service;

import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class TagService implements EntityService<Tag> {

    private static final String WRONG_DAO_MESSAGE = "Wrong DAO";
    private static TagService INSTANCE = new TagService();
    private EntityDAO<Tag> dao;
    private EntityValidator<Tag> validator;

    @Autowired
    public void setDao(EntityDAO<Tag> dao) {
        this.dao = dao;
    }

    @Autowired
    public void setValidator(EntityValidator<Tag> validator) {
        this.validator = validator;
    }

    private TagService() {
    }

    public static EntityService<Tag> getInstance() {
        return INSTANCE;
    }

    @Override
    public Tag create(Tag tag) throws ServiceException, ValidationException {
        try {
            validator.validate(tag);
            return dao.create(tag);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tag read(int id) throws ServiceException {
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
    public void update(Tag tag) throws ServiceException, ValidationException {
        try {
            validator.validate(tag);
            dao.update(tag);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Collection<Tag> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public Collection<Tag> findAll(Certificate certificate) throws ServiceException {
        try {
            if (dao instanceof TagDAO) {
                return ((TagDAO)dao).findAllByCertificate(certificate);
            } else {
                throw new ServiceException(WRONG_DAO_MESSAGE);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public Collection<Tag> findBy(EntityFinder<Tag> entityFinder) throws ServiceException {
        if (dao instanceof TagDAO) {
            try {
                return ((TagDAO)dao).findBy(entityFinder);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

    public Collection<Tag> find(String name, Map<String, String> sorting) throws ServiceException {
        if (dao instanceof TagDAO) {
            try {
                TagFinder finder = new TagFinder();
                if (StringUtils.isNotEmpty(name)) {
                    finder.findByName(name);
                }
                if (sorting != null) {
                    if (sorting.containsKey(sorting.get("sort-by-name"))) {
                        finder.sortByName(AscDesc.getValue(sorting.get("sort-by-name")));
                    }
                }
                return ((TagDAO)dao).findBy(finder);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        } else {
            throw new ServiceException(WRONG_DAO_MESSAGE);
        }
    }

}
