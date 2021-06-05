package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.util.MultiValueMap;

import javax.persistence.Entity;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * {@link CustomEntity} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityService<T extends CustomEntity> {
    String notFoundErrorMessage = "Requested entity not found(%s = %s)!";

    /**
     * Create {@link CustomEntity} and add it to database method.
     *
     * @param t {@link CustomEntity} to create.
     */
    T create(T t) throws ValidationException, BadRequestException;


    EntityFinder<T> getFinder();

    /**
     * Read {@link CustomEntity} from database method.
     *
     * @param id ID of the Entity.
     */
    T getById(int id) throws NotFoundException;

    /**
     * Delete {@link CustomEntity} from database method.
     *
     * @param id ID of the Entity.
     */
    void delete(int id) throws BadRequestException;

    /**
     * Update {@link CustomEntity} in database method.
     *
     * @param t {@link CustomEntity} to create.
     * @return Updated {@link Certificate}.
     */
    T update(T t) throws ValidationException, BadRequestException, NotFoundException;

    /**
     * Find all  {@link CustomEntity} objects in database method.
     *
     * @return list of founded {@link CustomEntity} objects.
     * @throws NotFoundException in case of malfunctioning, e.g..
     *
     */
    List<T> findAll() throws NotFoundException;

    /**
     * Find {@link Entity} objects by parameters method.
     *
     * @param params finding and sorting parameters.
     */
    List<T> findByParameters(MultiValueMap<String, String> params) throws BadRequestException, NotFoundException;

    /**
     * Decode request parameters using UTF-8.
     *
     * @return {@link String} decoded parameters.
     *
     */
    default String decodeParam(String param) {
        return URLDecoder.decode(param, StandardCharsets.UTF_8);
    }

    default T validateEntity(Optional<T> entityOptional, String parameterName,
                             String parameterValue, int errorCode)
            throws NotFoundException {
        return entityOptional.orElseThrow(
                () -> new NotFoundException(String.format(
                        notFoundErrorMessage, parameterName, parameterValue), errorCode));
    }
}
