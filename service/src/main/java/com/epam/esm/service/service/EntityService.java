package com.epam.esm.service.service;

import com.epam.esm.persistence.util.AscDesc;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import java.util.Collection;

public interface EntityService<T extends Entity> {

    T create (T t) throws ServiceException, ValidationException;

    T read (int id) throws ServiceException;

    void delete (int id) throws ServiceException;

    void update (T t) throws ServiceException, ValidationException;

    Collection<T> findAll() throws ServiceException;

    Collection<T> findBy(EntityFinder<T> entityFinder) throws ServiceException;

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
