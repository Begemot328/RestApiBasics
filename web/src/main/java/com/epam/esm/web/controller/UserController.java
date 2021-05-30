package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.constants.OrderSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.order.OrderService;
import com.epam.esm.service.service.order.OrderServiceImpl;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.service.service.user.UserServiceImpl;
import com.epam.esm.web.dto.order.OrderDTO;
import com.epam.esm.web.dto.order.OrderDTOMapper;
import com.epam.esm.web.dto.user.UserDTO;
import com.epam.esm.web.dto.user.UserDTOMapper;
import com.epam.esm.web.security.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
public class UserController implements PaginableSearch {
    private final OrderService orderService;
    private final UserService userService;
    private final CertificateService certificateService;
    private final UserDTOMapper userDTOMapper;
    private final OrderDTOMapper orderDTOMapper;

    @Autowired
    public UserController(OrderServiceImpl orderService,
                          UserServiceImpl userServiceImpl,
                          UserDTOMapper userDTOMapper,
                          OrderDTOMapper orderDTOMapper,
                          CertificateService certificateService) {
        this.orderService = orderService;
        this.userService = userServiceImpl;
        this.userDTOMapper = userDTOMapper;
        this.orderDTOMapper = orderDTOMapper;
        this.certificateService = certificateService;
    }

    @Secured(Roles.ADMIN)
    @GetMapping
    public ResponseEntity<?> find(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<User> users;
        if (CollectionUtils.isEmpty(params)) {
            users = userService.findAll();
        } else {
            users = userService.findByParameters(params);
        }
        CollectionModel<UserDTO> userDTOs = userDTOMapper.toUserDTOList(
                users);
        userDTOs.add(linkTo(methodOn(this.getClass()).find(params)).withRel("users"));

        paginate(params, userDTOs);

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') and hasPermission(#id,'user_permission')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") int id) throws NotFoundException {
        User user = userService.checkIfPresent(userService.getById(id),
                "id", Integer.toString(id), ErrorCodes.USER_NOT_FOUND);
        final UserDTO userDTO = userDTOMapper.toUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') and hasPermission(#id,'user_permission')")
    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<?> readUsersOrders(@PathVariable(value = "id") int id,
                                             @RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        params.put(OrderSearchParameters.USER_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        List<Order> orders = orderService.findByParameters(params);
        CollectionModel<OrderDTO> orderDTOs = orderDTOMapper.toOrderDTOList(orders);
        orderDTOs.add(linkTo(methodOn(this.getClass()).readUsersOrders(id, params)).withRel("tags"));

        paginate(params, orderDTOs);

        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') and hasPermission(#id,'user_permission')")
    @GetMapping(value = "/{id}/orders/{order_id}")
    public ResponseEntity<?> readOrder(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "order_id") int orderId,
                                       @RequestParam MultiValueMap<String, String> params)
            throws NotFoundException {
        params.put(OrderSearchParameters.USER_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        params.put(OrderSearchParameters.ORDER_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        OrderDTO order = orderDTOMapper.toOrderDTO(orderService.checkIfPresent(orderService.getById(orderId),
                "id", Integer.toString(orderId), ErrorCodes.ORDER_NOT_FOUND));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') and hasPermission(#id,'user_permission')")
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}")
    public ResponseEntity<?> createOrder(@PathVariable(value = "id") int id,
                                         @RequestParam int certificateId,
                                         @RequestParam int quantity)
            throws NotFoundException, ValidationException, BadRequestException {
        User user = userService.checkIfPresent(userService.getById(id),
                "id", Integer.toString(id), ErrorCodes.USER_NOT_FOUND);
        Certificate certificate = certificateService.checkIfPresent(
                certificateService.getById(certificateId),
                "id", Integer.toString(certificateId),
                ErrorCodes.CERTIFICATE_NOT_FOUND);
        Order order = orderService.createOrder(certificate, user, quantity);
        return new ResponseEntity<>(orderDTOMapper.toOrderDTO(order), HttpStatus.OK);
    }
}
