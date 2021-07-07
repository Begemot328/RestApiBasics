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
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
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
        }
        return tagOptional.get();
    }

    @Override
    public Optional<Tag> readOptional(int id) {
        return Optional.ofNullable(dao.getById(id));
    }

    @Override
    @Transactional
    public void delete(int id) throws BadRequestException {
        if (dao.getById(id) == null) {
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
        List<Tag> tags = dao.findAll();
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("No tags found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
        }
    }

    private List<Tag> readBy(EntityFinder<Tag> entityFinder) throws NotFoundException {
        List<Tag> tags = dao.findByParameters(entityFinder);
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

    @Lookup
    @Override
    public TagFinder getFinder() {
        return null;
    }

    @Override
    public List<Tag> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException {
        TagFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                parseParameter(finder, entry.getKey(), entry.getValue());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    private void parseParameter(TagFinder finder, String parameterName, List<String> parameterValues) {
        if (parameterName.contains("sort")) {
            parseSortParameter(finder, parameterName, parameterValues);
        } else if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, parameterValues);
        } else {
            parseFindParameter(finder, parameterName, parameterValues);
        }
    }

    private void parseSortParameter(TagFinder finder, String parameterName, List<String> parameterValues) {
        finder.sortBy(TagSortingParameters.getEntryByParameter(parameterName).getValue(),
                SortDirection.parseAscDesc(parameterValues.get(0)));
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterName, List<String> parameterValues) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(parameterValues.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(parameterValues.get(0)));
                break;
        }
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.TAG_BAD_REQUEST);
        }
    }

    @Override
    public Tag readMostlyUsedTag() throws NotFoundException {
        Tag tag = dao.findMostPopularTag();
        if (tag == null) {
            throw new NotFoundException("Requested resource not found (most popular tag)!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tag;
        }
    }
}
