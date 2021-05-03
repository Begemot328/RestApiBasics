package com.epam.esm.service.service.order;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAO;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;
import com.epam.esm.persistence.util.OrderFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.OrderSearchParameters;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderDAO dao;
    private EntityValidator<Order> validator;

    @Autowired
    public OrderServiceImpl(OrderDAO dao,
                            EntityValidator<Order> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public Order createOrder(Certificate certificate, User user, int quantity) throws ServiceException, ValidationException {
        Order order = new Order(certificate, user,
                certificate.getPrice().multiply(BigDecimal.valueOf(quantity)),
                quantity, LocalDateTime.now());
        return create(order);
    }

    @Override
    public Order create(Order order) throws ServiceException, ValidationException {
        try {
            order.setPurchaseDate(LocalDateTime.now());
            validator.validate(order);
            return dao.create(order);
        } catch (DAOSQLException e) {
            throw new ServiceException(e, ErrorCodes.ORDER_INTERNAL_ERROR);
        }
    }

    @Override
    public Order read(int id) throws NotFoundException {
        Order order = dao.read(id);
        if (order == null) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return order;
        }
    }

    @Override
    public void delete(int id) throws BadRequestException {
        if (dao.read(id) == null) {
            throw new BadRequestException("Entity does not exist", ErrorCodes.ORDER_BAD_REQUEST);
        }
        dao.delete(id);
    }

    @Override
    public Order update(Order order) {
        throw new UnsupportedOperationException("Update operation for order is unavailable");
    }

    @Override
    public List<Order> readAll() throws NotFoundException {
        List<Order> orders = dao.readAll();
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("No orders found!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    private List<Order> readBy(EntityFinder<Order> entityFinder) throws NotFoundException {
        List<Order> orders = dao.readBy(entityFinder);
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    @Override
    public List<Order> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException {
        OrderFinder finder = new OrderFinder();
        for (String key : params.keySet()) {
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(params.get(key))) {
                throw new BadRequestException("Empty parameter!", ErrorCodes.ORDER_BAD_REQUEST);
            }
            try {
                 if (PaginationParameters.contains(key)) {
                    parsePaginationParameter(finder, key, params.get(key));
                } else {
                     parseFindParameter(finder, key, params.get(key));
                 }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.ORDER_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    private void parseFindParameter(OrderFinder finder, String parameterString, List<String> list) {
        OrderSearchParameters parameter =
                OrderSearchParameters.getEntryByParameter(parameterString);
        if (parameter.equals(OrderSearchParameters.USER_ID)) {
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
                    finder.findByUser(Integer.parseInt(list.get(0)));
                }
        }
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterString, List<String> list) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(list.get(0)));
                break;
            case OFFSET:
                finder.offset(Integer.parseInt(list.get(0)));
                break;
        }
    }
}
