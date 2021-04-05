package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {

    private TagDAO dao;
    private EntityValidator<Tag> validator;
    private TagFinder finder;

    @Autowired
    public TagServiceImpl(TagDAO dao,
                          EntityValidator<Tag> validator,
                          TagFinder finder) {
        this.dao = dao;
        this.validator = validator;
        this.finder = finder;
    }

    @Override
    public Tag create(Tag tag) throws ServiceException, ValidationException {
        try {
            validator.validate(tag);
            return dao.create(tag);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tag read(int id) throws ServiceException {
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
                throw new ServiceException("Entity does not exist");
            }
            dao.delete(id);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Tag tag) throws ServiceException, ValidationException {
        try {
            validator.validate(tag);
            dao.update(tag);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Collection<Tag> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tag> findBy(EntityFinder<Tag> entityFinder) throws ServiceException {
        try {
            return dao.findBy(entityFinder);
        } catch (DAOSQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tag> findByCertificate(int certificateId) throws ServiceException {
        finder = new TagFinder();
        finder.findByCertificate(certificateId);
        return findBy(finder);
    }

    @Override
    public List<Tag> find(Map<String, String> params) throws ServiceException {
        finder = new TagFinder();
        for (String key : params.keySet()) {
            try {
                if (key.contains("sort")) {
                    finder.sortBy(TagSortingParameters.getEntryByParameter(key).getValue(),
                            parseAscDesc(params.get(key)));
                } else {
                    TagSearchParameters optional =
                            TagSearchParameters.getEntryByParameter(key);
                    if (optional == TagSearchParameters.NAME) {
                        finder.findByName(URLDecoder.decode(params.get(key), StandardCharsets.UTF_8));
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e);
            }
        }
        return findBy(finder);
    }
}
