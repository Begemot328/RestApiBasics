package com.epam.esm.services.service.tag;

import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import com.epam.esm.services.service.EntityService;
import com.epam.esm.services.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class TagService implements EntityService<Tag> {

    @Autowired
    private static TagService INSTANCE;

    private TagDAO dao;
    private EntityValidator<Tag> validator;
    private TagFinder finder;

    @Autowired
    private TagService(TagDAO dao,
                       EntityValidator<Tag> validator,
                       TagFinder finder) {
        this.dao = dao;
        this.validator = validator;
        this.finder = finder;
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
            if (dao.read(id) == null) {
                throw new ServiceException("Entity does not exist");
            }
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
            return dao.findAllByCertificate(certificate);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public Collection<Tag> findBy(EntityFinder<Tag> entityFinder) throws ServiceException {
        try {
            return ((TagDAO) dao).findBy(entityFinder);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public Collection<Tag> find(Map<String, String> params) throws ServiceException {
        finder.newFinder();
        for (String key : params.keySet()) {
            if (key.contains("sort")) {
                finder.sortBy(TagSortingParameters.getEntryByParameter(key).getValue(),
                        AscDesc.valueOf(params.get(key)));
            } else {
                Optional<TagSearchParameters> optional =
                        Optional.ofNullable(TagSearchParameters.getEntryByParameter(key));
                if (optional.isPresent()) {
                    if (optional.get() == TagSearchParameters.NAME) {
                        finder.findByName(params.get(key));
                    }
                }
            }
        }
        return findBy(finder);
    }

}
