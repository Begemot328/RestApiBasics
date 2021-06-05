package com.epam.esm.service.service.order;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.impl.OrderFinder;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.OrderSearchParameters;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.EntityService;
import com.epam.esm.service.validator.EntityValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link Order} service implementation.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final String NOT_FOUND_ERROR_MESSAGE = "Requested order not found(%s = %s)!";

    private final OrderDAO dao;
    private final EntityValidator<Order> validator;
    private final EntityService<Certificate> certificateService;
    private final EntityService<User> userService;

    @Autowired
    public OrderServiceImpl(OrderDAO dao,
                            EntityValidator<Order> validator,
                            EntityService<User> userService, EntityService<Certificate> certificateService) {
        this.dao = dao;
        this.validator = validator;
        this.certificateService = certificateService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Order createOrder(int certificateId, int userId, int quantity)
            throws ValidationException, BadRequestException, NotFoundException {
        User user = userService.getById(userId);
        Certificate certificate = certificateService.getById(certificateId);
        Order order = new Order(certificate, user,
                certificate.getPrice().multiply(BigDecimal.valueOf(quantity)),
                quantity, LocalDateTime.now());

        validator.validate(order);
        return dao.save(order);
    }

    @Override
    @Transactional
    public Order create(Order order) throws ValidationException, BadRequestException {
        throw new UnsupportedOperationException("Create order using certificate and user ID's!");
    }

    @Override
    public Order getById(int id) throws NotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, "id", id),
                        ErrorCodes.ORDER_NOT_FOUND));
    }

    @Override
    public void delete(int id) throws BadRequestException {
        Optional<Order> order = dao.findById(id);
        dao.delete(order.orElseThrow(
                () -> new BadRequestException("Entity does not exist", ErrorCodes.ORDER_BAD_REQUEST)));
    }

    @Override
    public Order update(Order order) {
        throw new UnsupportedOperationException("Update operation for order is unavailable");
    }

    @Override
    public List<Order> findAll() throws NotFoundException {
        List<Order> orders = IterableUtils.toList(dao.findAll());
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("No orders found!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    private List<Order> readBy(OrderFinder entityFinder) throws NotFoundException {
        List<Order> orders = dao.findAll(
                entityFinder.getPredicate(), entityFinder.getPaginationAndSorting())
                .getContent();
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("Requested resource not found!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    @Lookup
    @Override
    public OrderFinder getFinder() {
        return null;
    }

    @Override
    public List<Order> findByParameters(MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        OrderFinder finder = getFinder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            try {
                validateParameterValues(entry.getValue());
                parseParameter(finder, entry.getKey(), entry.getValue());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e, ErrorCodes.ORDER_BAD_REQUEST);
            }
        }
        return readBy(finder);
    }

    private void parseParameter(OrderFinder finder,
                                String parameterName,
                                List<String> parameterValues) {
        if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, parameterValues);
        } else {
            parseFindParameter(finder, parameterName, parameterValues);
        }
    }

    private void parseFindParameter(OrderFinder finder, String parameterName, List<String> list) {
        OrderSearchParameters parameter =
                OrderSearchParameters.getEntryByParameter(parameterName);
        switch (parameter) {
            case ORDER_ID:
                finder.findById(Integer.parseInt(list.get(0)));
                break;
            case USER_ID:
                finder.findByUser(Integer.parseInt(list.get(0)));
                break;
        }
    }

    private void validateParameterValues(List<String> parameterValues) throws BadRequestException {
        if (CollectionUtils.isEmpty(parameterValues)) {
            throw new BadRequestException("Empty parameter!", ErrorCodes.USER_BAD_REQUEST);
        }
    }

    private void parsePaginationParameter(EntityFinder finder, String parameterString, List<String> list) {
        PaginationParameters parameter = PaginationParameters.getEntryByParameter(parameterString);
        switch (parameter) {
            case LIMIT:
                finder.limit(Integer.parseInt(list.get(0)));
                break;
            case PAGE:
                finder.page(Integer.parseInt(list.get(0)));
                break;
        }
    }
}
