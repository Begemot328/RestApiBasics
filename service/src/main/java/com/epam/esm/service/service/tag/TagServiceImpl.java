package com.epam.esm.service.service.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.SortDirection;
import com.epam.esm.persistence.util.finder.impl.TagFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.constants.TagSearchParameters;
import com.epam.esm.service.constants.TagSortingParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class TagServiceImpl implements TagService {
    private final TagDAO dao;
    private final EntityValidator<Tag> validator;

    @Autowired
    public TagServiceImpl(TagDAO dao,
                          EntityValidator<Tag> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) throws ValidationException, BadRequestException {
        validator.validate(tag);
        try {
            return dao.create(tag);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
        }
    }

    @Override
    public Tag read(int id) throws NotFoundException {
        Optional<Tag> tagOptional = readOptional(id);
        if (tagOptional.isEmpty()) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tagOptional.get();
        }
    }

    @Override
    public Optional<Tag> readOptional(int id) {
        return Optional.ofNullable(dao.read(id));
    }

    @Override
    @Transactional
    public void delete(int id) throws BadRequestException {
        if (dao.read(id) == null) {
            throw new BadRequestException("Entity does not exist", ErrorCodes.TAG_BAD_REQUEST);
        }
        dao.delete(id);
    }

    @Override
    @Transactional
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


    private List<Tag> readBy(EntityFinder<Tag> entityFinder) throws NotFoundException {
        List<Tag> tags = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
        }
    }

    private void parseFindParameter(TagFinder finder, String parameterString, List<String> list) {
        TagSearchParameters parameter =
                TagSearchParameters.getEntryByParameter(parameterString);
        if (parameter == TagSearchParameters.NAME) {
            addToFinder(finder::findByName, list);
        }
    }

    private void addToFinder(Consumer<String> consumer, String value) {
        if (StringUtils.isNotEmpty(value)) {
            consumer.accept(decodeParam(value));
        }
    }

    private void addToFinder(Consumer<String> consumer, List<String> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            addToFinder(consumer, list.get(0));
        }
    }

    @Override
    public List<Tag> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException {
        TagFinder finder = new TagFinder(dao);
        for (String key : params.keySet()) {
            if (CollectionUtils.isEmpty(params.get(key))) {
                throw new BadRequestException("Empty parameter!", ErrorCodes.TAG_BAD_REQUEST);
            }
            try {
                if (key.contains("sort")) {
                    finder.sortBy(TagSortingParameters.getEntryByParameter(key).getValue(),
                            SortDirection.parseAscDesc(params.get(key).get(0)));
                } else if (PaginationParameters.contains(key)) {
                    parsePaginationParameter(finder, key, params.get(key));
                } else {
                    parseFindParameter(finder, key, params.get(key));
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterString, List<String> list) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(list.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(list.get(0)));
                break;
        }
    }

    @Override
    public Tag readMostlyUsedTag() throws NotFoundException {
        Tag tag = dao.readMostlyUsedTag();
        if (tag == null) {
            throw new NotFoundException("Requested resource not found (most popular tag)!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tag;
        }
    }
}
