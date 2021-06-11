package com.epam.esm.service.service.order;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.EntityService;

import javax.persistence.Entity;

/**
 * {@link Order} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderService extends EntityService<Order> {

    /**
     * Find {@link Entity} objects by parameters method.
     *
     * @param certificateId {@link Certificate} to order.
     * @param userId {@link User} to make order for.
     * @param quantity {@link Certificate} quantity to order.
     */
    Order createOrder(int certificateId, int userId, int quantity)
            throws ValidationException, NotFoundException;
}
