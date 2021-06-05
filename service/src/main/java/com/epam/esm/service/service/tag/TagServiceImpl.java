package com.epam.esm.service.service.tag;

import com.epam.esm.model.entity.Certificate;
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
import org.apache.commons.collections4.IterableUtils;
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
    private static final String NOT_FOUND_ERROR_MESSAGE = "Requested tag not found(%s = %s)!";

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
            if (getByName(tag.getName()).isPresent()) {
                throw new BadRequestException("Such name already exists! name= " + tag.getName(),
                        ErrorCodes.TAG_BAD_REQUEST);
            }
            return dao.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
        }
    }

    @Override
    public Tag getById(int id) throws NotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, "id", id),
                        ErrorCodes.TAG_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(int id) throws BadRequestException {
        Optional<Tag> tag = dao.findById(id);
        dao.delete(tag.orElseThrow(
                () -> new BadRequestException("Entity does not exist", ErrorCodes.TAG_BAD_REQUEST)));
    }

    @Override
    @Transactional
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException(
                "Update operation for tag is unavailable");
    }

    @Override
    public List<Tag> findAll() throws NotFoundException {
        List<Tag> tags = IterableUtils.toList(dao.findAll());
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("No tags found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
        }
    }

    private List<Tag> findByFinder(EntityFinder<Tag> entityFinder) throws NotFoundException {
        List<Tag> tags = dao.findByParameters(entityFinder);
        if (CollectionUtils.isEmpty(tags)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.TAG_NOT_FOUND);
        } else {
            return tags;
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
    public List<Tag> findByParameters(MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        TagFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                parseParameter(finder, entry.getKey(), entry.getValue());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.TAG_BAD_REQUEST);
            }
        }
        return findByFinder(finder);
    }

    private void parseParameter(TagFinder finder, String parameterName, List<String> parameterValues) {
        if (parameterName.contains("sort")) {
            addSortingToFinder(finder, getSortingParameter(parameterName),
                    getSortDirection(parameterValues));
        } else if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, Integer.parseInt(parameterValues.get(0)));
        } else {
            parseFindParameter(finder, parameterName, parameterValues);
        }
    }

    private void parseFindParameter(TagFinder finder, String parameterString, List<String> list) {
        TagSearchParameters parameter =
                TagSearchParameters.getEntryByParameter(parameterString);
        if (parameter == TagSearchParameters.NAME) {
            addToFinder(finder::findByNameLike, list);
        }
    }

    private void addSortingToFinder(TagFinder finder, String sortingParameter, SortDirection sortDirection) {
        finder.sortBy(sortingParameter, sortDirection);
    }

    private String getSortingParameter(String parameterName) {
        return TagSortingParameters.getEntryByParameter(parameterName).getValue();
    }

    private SortDirection getSortDirection(List<String> parameterValues) {
        return SortDirection.parseAscDesc(parameterValues.get(0));
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterName, int quantityOfElements) {
        switch (getPaginationParameterName(parameterName)) {
            case LIMIT:
                finder.limit(quantityOfElements);
                break;
            case OFFSET:
                finder.offset(quantityOfElements);
                break;
        }
    }

    private PaginationParameters getPaginationParameterName(String parameterName) {
        return PaginationParameters.getEntryByParameter(parameterName);
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.TAG_BAD_REQUEST);
        }
    }

    @Override
    public Tag findMostPopularTag() throws NotFoundException {
        Optional<Tag> tag = dao.findMostPopularTag();
        return tag.orElseThrow(
                () -> new NotFoundException("Requested resource not found (most popular tag)!",
                        ErrorCodes.TAG_NOT_FOUND));

    }

    @Override
    public Optional<Tag> getByName(String name) {
        TagFinder finder = getFinder();
        addToFinder(finder::findByName, name);
        List<Tag> tags = dao.findByParameters(finder);
        if (CollectionUtils.isEmpty(tags)) {
            return Optional.empty();
        }
        return Optional.of(tags.get(0));
    }

    @Override
    public void makeAllTagsDetached(List<Tag> tags)
            throws BadRequestException, ValidationException {
        for (Tag tag : tags) {
            if (dao.findById(tag.getId()).isEmpty()) {
                Optional<Tag> tagOptional = getByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tag.setId(tagOptional.get().getId());
                } else {
                    create(tag);
                }
            }
        }
    }
}
