package com.epam.esm.services.service;

import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.model.entity.Entity;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.exceptions.ValidationException;
import java.util.Collection;

public abstract class AbstractEntityService<T extends Entity> implements EntityService<T>{

    @Override
    public abstract T create(T t) throws ServiceException, ValidationException;

    @Override
    public abstract T read(int id) throws ServiceException;

    @Override
    public abstract void delete(int id) throws ServiceException;

    @Override
    public abstract void update(T t) throws ServiceException, ValidationException;

    @Override
    public abstract Collection<T> findAll() throws ServiceException;

    @Override
    public abstract Collection<T> findBy(EntityFinder<T> finder) throws ServiceException;
}
