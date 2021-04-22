package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.util.SortDirection;
import com.epam.esm.persistence.util.TagFinder;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
            throw new ServiceException(e, ErrorCodes.TAG_INTERNAL_ERROR);
        } catch (ValidationException e) {
            throw new ServiceException(e, ErrorCodes.TAG_VALIDATION_EXCEPTION);
        }
    }

    @Override
    public Tag read(int id) throws NotFoundException {
        Tag tag = dao.read(id);
        if (tag == null) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tag;
        }
    }

    @Override
    public void delete(int id) throws BadRequestException {
            if (dao.read(id) == null) {
                throw new BadRequestException("Entity does not exist", ErrorCodes.TAG_BAD_REQUEST);
            }
            dao.delete(id);
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update operation for tag is unavailable");
    }

    @Override
    public List<Tag> readAll() throws NotFoundException {
        List<Tag> tags = dao.readAll();
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("No tags found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
        }
    }

    @Override
    public List<Tag> readBy(EntityFinder<Tag> entityFinder) throws NotFoundException {
        List<Tag> tags = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
        }
    }

    @Override
    public List<Tag> readByCertificate(int certificateId) throws NotFoundException {
        TagFinder finder = new TagFinder();
        finder.findByCertificate(certificateId);
        return readBy(finder);
    }

    private void parseFindParameter(TagFinder finder, String parameterString, String value) {
        TagSearchParameters parameter =
                TagSearchParameters.getEntryByParameter(parameterString);
        if (parameter == TagSearchParameters.NAME) {
            addToFinder(finder::findByName, value);
        }
    }

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }

    @Override
    public List<Tag> read(Map<String, String> params) throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder();
        for (String key : params.keySet()) {
            try {
                if (key.contains("sort")) {
                    finder.sortBy(TagSortingParameters.getEntryByParameter(key).getValue(),
                            SortDirection.parseAscDesc(params.get(key)));
                } else {
                    parseFindParameter(finder, key, params.get(key));
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    @Override
    public Tag readMostlyUsedTag() throws BadRequestException, NotFoundException {
        return dao.readMostlyUsedTag();
    }
}
