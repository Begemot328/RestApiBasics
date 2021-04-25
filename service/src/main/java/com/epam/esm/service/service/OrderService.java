package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;

import java.util.List;

/**
 * {@link Order} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderService extends EntityService<Order> {
    Order createOrder(Certificate certificate, User user, int quantity) throws ServiceException, ValidationException;

    List<Order> readByUser(int id) throws NotFoundException;
}
