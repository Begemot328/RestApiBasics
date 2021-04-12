package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    private TagDAO dao;
    private EntityValidator<Tag> validator;

    @Autowired
    public TagServiceImpl(TagDAO dao,
                          EntityValidator<Tag> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public Tag create(Tag tag) throws ServiceException {
        try {
            validator.validate(tag);
            return dao.create(tag);
        } catch (DAOSQLException e) {
            throw new ServiceException(e, ErrorCodes.TAG_ERROR_CODE_SUFFIX, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ValidationException e) {
            throw new ServiceException(e, ErrorCodes.TAG_ERROR_CODE_SUFFIX, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public Tag read(int id) throws ServiceException {
            return dao.read(id);
    }

    @Override
    public void delete(int id) throws ServiceException {
            if (dao.read(id) == null) {
                throw new ServiceException("Entity does not exist", ErrorCodes.TAG_ERROR_CODE_SUFFIX,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            dao.delete(id);
    }

    @Override
    public Certificate update(Tag tag) {
        throw new UnsupportedOperationException("Update operation for tag is unavailable");
    }

    @Override
    public List<Tag> readAll() {
            return dao.readAll();
    }

    @Override
    public List<Tag> readBy(EntityFinder<Tag> entityFinder) {
            return dao.readBy(entityFinder);
    }

    @Override
    public List<Tag> readByCertificate(int certificateId) throws ServiceException {
        TagFinder finder = new TagFinder();
        finder.findByCertificate(certificateId);
        return readBy(finder);
    }

    @Override
    public List<Tag> read(Map<String, String> params) throws ServiceException, BadRequestException {
        TagFinder finder = new TagFinder();
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
                throw new BadRequestException(e, ErrorCodes.TAG_ERROR_CODE_SUFFIX, HttpStatus.BAD_REQUEST);
            }
        }
        return readBy(finder);
    }
}
