package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.util.SortDirection;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import org.springframework.http.HttpStatus;

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
    T create (T t) throws ServiceException;

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
     * @return Updated {@link Certificate}
     */
    Certificate update (T t) throws ServiceException;

    /**
     * Find all  {@link Entity} objects in database method
     *
     * @throws ServiceException in case of malfunctioning, e.g.
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @return list of founded {@link Entity} objects
     */
    List<T> readAll() throws ServiceException;

    /**
     * Find {@link Entity} objects by {@link EntityFinder} criteria in database method
     *
     * @throws ServiceException in case of malfunctioning, e.g.
     * {@link com.epam.esm.persistence.exceptions.DAOSQLException}
     * @param entityFinder {@link EntityFinder} criteria to find objects
     * @return list of founded {@link Entity} objects
     */
    List<T> readBy(EntityFinder<T> entityFinder) throws ServiceException;

    /**
     * Obtain {@link SortDirection} enum element
     *
     * @param param name of the parameter
     * @throws BadRequestException in case of bad request parameters, e.g. param does not
     * correspond to AscDesc elements
     * @return {@link SortDirection} element
     */
    default SortDirection parseAscDesc(String param) throws BadRequestException {
        switch (param) {
            case "1":
            case "asc":
                return SortDirection.ASC;
            case "2":
            case "desc":
                return SortDirection.DESC;
            default:
                throw new BadRequestException("Wrong parameter sort-by!", 0, HttpStatus.BAD_REQUEST);
        }
    }
}
