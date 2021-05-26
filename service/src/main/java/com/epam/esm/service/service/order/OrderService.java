package com.epam.esm.service.service.order;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
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
     * @param certificate {@link Certificate} to order.
     * @param user {@link User} to make order for.
     * @param quantity {@link Certificate} quantity to order.
     */
    Order createOrder(Certificate certificate, User user, int quantity)
            throws ValidationException, BadRequestException;
}
