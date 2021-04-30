package com.epam.esm.service.service.order;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.EntityService;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * {@link Order} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderService extends EntityService<Order> {
    Order createOrder(Certificate certificate, User user, int quantity) throws ServiceException, ValidationException;

    List<Order> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException;
}
