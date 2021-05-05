package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CustomEntity;
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

    /**
     * Create {@link CustomEntity} and add it to database method.
     *
     * @param t {@link CustomEntity} to create.
     */
    T create(T t) throws ValidationException, BadRequestException;

    /**
     * Read {@link CustomEntity} from database method.
     *
     * @param id ID of the Entity.
     */
    T read(int id) throws NotFoundException;

    /**
     * Read {@link CustomEntity} from database method.
     *
     * @param id ID of the Entity.
     */
    Optional<T> readOptional(int id);

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
    List<T> readAll() throws NotFoundException;

    /**
     * Find {@link Entity} objects by parameters method.
     *
     * @param params finding and sorting parameters.
     */
    List<T> read(MultiValueMap<String, String> params) throws BadRequestException, NotFoundException;

    /**
     * Decode request parameters using UTF-8.
     *
     * @return {@link String} decoded parameters.
     *
     */
    default String decodeParam(String param) {
        return URLDecoder.decode(param, StandardCharsets.UTF_8);
    }
}
