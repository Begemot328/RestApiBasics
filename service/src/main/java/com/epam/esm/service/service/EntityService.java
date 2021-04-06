package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import java.util.Collection;
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
    T read (int id) throws ServiceException;

    /**
     * Delete {@link Entity} from database method
     *
     * @param id ID of the Entity
     *
     */
    void delete (int id) throws ServiceException;

    /**
     * Update {@link Entity} in database method
     *
     * @param t {@link Entity} to create
     *
     */
    void update (T t) throws ServiceException, ValidationException;

    /**
     * Find all  {@link Entity} objects in database method
     *
     * @throws ServiceException in case of malfunctioning, e.g.
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @return list of founded {@link Entity} objects
     */
    List<T> findAll() throws ServiceException;

    /**
     * Find {@link Entity} objects by {@link EntityFinder} criteria in database method
     *
     * @throws ServiceException in case of malfunctioning, e.g.
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @param entityFinder {@link EntityFinder} criteria to find objects
     * @return list of founded {@link Entity} objects
     */
    List<T> findBy(EntityFinder<T> entityFinder) throws ServiceException;

    /**
     * Obtain {@link AscDesc} enum element
     *
     * @param param name of the parameter
     * @throws BadRequestException in case of bad request parameters, e.g. param does not
     * correspond to AscDesc elements
     * @return {@link AscDesc} element
     */
    default AscDesc parseAscDesc(String param) throws BadRequestException {
        switch (param) {
            case "true":
                return AscDesc.ASC;
            case "false":
                return AscDesc.DESC;
            case "1":
                return AscDesc.ASC;
            case "2":
                return AscDesc.DESC;
            case "asc":
                return AscDesc.ASC;
            case "desc":
                return AscDesc.DESC;
            default:
                throw new BadRequestException("Wrong parameter sort-by!");
        }
    }
}
