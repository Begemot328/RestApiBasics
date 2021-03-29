package com.epam.esm.services.service;

import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;

import java.util.Collection;

public interface EntityService<T extends Entity> {

    T create (T t) throws ServiceException, ValidationException;

    T read (int id) throws ServiceException;

    void delete (int id) throws ServiceException;

    void update (T t) throws ServiceException, ValidationException;

    Collection<T> findAll() throws ServiceException;

    Collection<T> findBy(EntityFinder<T> entityFinder) throws ServiceException;
}
