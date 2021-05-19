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
    public Order createOrder(Certificate certificate, User user, int quantity)
            throws ValidationException, BadRequestException {

        Order order = new Order(certificate, user,
                certificate.getPrice().multiply(BigDecimal.valueOf(quantity)),
                quantity, LocalDateTime.now());
        return create(order);
    }

    @Override
    @Transactional
    public Order create(Order order) throws ValidationException, BadRequestException {

        order.setPurchaseDate(LocalDateTime.now());
        validator.validate(order);

        if (certificateService.readOptional(order.getCertificate().getId()).isEmpty()) {
            throw new BadRequestException("Unknown certificate!", ErrorCodes.ORDER_BAD_REQUEST);
        }
        if (userService.readOptional(order.getUser().getId()).isEmpty()) {
            throw new BadRequestException("Unknown user!", ErrorCodes.ORDER_BAD_REQUEST);
        }

        return dao.create(order);

    }

    @Override
    public Order read(int id) throws NotFoundException {
        Optional<Order> orderOptional = readOptional(id);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Requested resource not found(id = " + id + ")!",
                    ErrorCodes.ORDER_NOT_FOUND);
        }
        return orderOptional.get();
    }

    @Override
    public Optional<Order> readOptional(int id) {
        return Optional.ofNullable(dao.getById(id));
    }

    @Override
    public void delete(int id) throws BadRequestException {
        if (dao.getById(id) == null) {
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
        List<Order> orders = dao.findAll();
        if (CollectionUtils.isEmpty(orders)) {
            throw new NotFoundException("No orders found!",
                    ErrorCodes.ORDER_NOT_FOUND);
        } else {
            return orders;
        }
    }

    private List<Order> readBy(EntityFinder<Order> entityFinder) throws NotFoundException {
        List<Order> orders = dao.findByParameters(entityFinder);
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
    public List<Order> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException {
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

    private void parseParameter(OrderFinder finder, String parameterName, List<String> parameterValues) {
        if (PaginationParameters.contains(parameterName)) {
            parsePaginationParameter(finder, parameterName, parameterValues);
        } else {
            parseFindParameter(finder, parameterName, parameterValues);
        }
    }

    private void parseFindParameter(OrderFinder finder, String parameterName, List<String> list) {
        OrderSearchParameters parameter =
                OrderSearchParameters.getEntryByParameter(parameterName);
        if (parameter.equals(OrderSearchParameters.USER_ID)
                && CollectionUtils.isNotEmpty(list)) {
            finder.findByUser(Integer.parseInt(list.get(0)));
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
            case OFFSET:
                finder.offset(Integer.parseInt(list.get(0)));
                break;
        }
    }
}
