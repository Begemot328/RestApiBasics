package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.util.SortDirection;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * {@link Entity} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityService<T extends Entity> {

    /**
     * Create {@link Entity} and add it to database method
     *
     * @param t {@link Entity} to create
     *
     */
    T create (T t) throws ServiceException, ValidationException;

    /**
     * Read {@link Entity} from database method
     *
     * @param id ID of the Entity
     *
     */
    T read (int id) throws NotFoundException;

    /**
     * Delete {@link Entity} from database method
     *
     * @param id ID of the Entity
     *
     */
    void delete (int id) throws BadRequestException;

    /**
     * Update {@link Entity} in database method
     *
     * @param t {@link Entity} to create
     *
     * @return Updated {@link Certificate}
     */
    Certificate update (T t) throws ValidationException;

    /**
     * Find all  {@link Entity} objects in database method
     *
     * @throws NotFoundException in case of malfunctioning, e.g.
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @return list of founded {@link Entity} objects
     */
    List<T> readAll() throws NotFoundException;

    /**
     * Find {@link Entity} objects by {@link EntityFinder} criteria in database method
     *
     * @throws NotFoundException if nothing was found
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @param entityFinder {@link EntityFinder} criteria to find objects
     * @return list of founded {@link Entity} objects
     */
    List<T> readBy(EntityFinder<T> entityFinder) throws NotFoundException;

    default String decodeParam(String param) {
        return URLDecoder.decode(param, StandardCharsets.UTF_8);
    }
}
