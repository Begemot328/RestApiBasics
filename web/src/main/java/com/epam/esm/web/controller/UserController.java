package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.constants.OrderSearchParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
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
import com.epam.esm.web.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/")
public class UserController {
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

    @GetMapping(value = "/users")
    public ResponseEntity<?> readUsers(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<User> users;
        if (CollectionUtils.isEmpty(params)) {
            users = userService.readAll();
        } else {
            users = userService.read(params);
        }
        CollectionModel<UserDTO> userDTOs = userDTOMapper.toUserDTOList(
                users);
        Link link = linkTo(methodOn(this.getClass()).readUsers(params)).withRel("users");
        userDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if (paginator.isLimited(params)) {
            userDTOs.add(linkTo(methodOn(this.getClass()).readUsers(
                    paginator.nextPage(params))).withRel("nextPage"));
            userDTOs.add(linkTo(methodOn(this.getClass()).readUsers(
                    paginator.previousPage(params))).withRel("previousPage"));
        }

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<?> readAllOrders(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<Order> orders;
        if (CollectionUtils.isEmpty(params)) {
            orders = orderService.readAll();
        } else {
            orders = orderService.read(params);
        }
        CollectionModel<OrderDTO> orderDTOs = orderDTOMapper.toOrderDTOList(
                orders);
        Link link = linkTo(methodOn(this.getClass()).readAllOrders(params)).withRel("orders");
        orderDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if (paginator.isLimited(params)) {
            orderDTOs.add(linkTo(methodOn(this.getClass()).readAllOrders(
                    paginator.nextPage(params))).withRel("nextPage"));
            orderDTOs.add(linkTo(methodOn(this.getClass()).readAllOrders(
                    paginator.previousPage(params))).withRel("previousPage"));
        }
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> readUser(@PathVariable(value = "id") int id) throws NotFoundException {
        User user = userService.read(id);
        final UserDTO userDTO = userDTOMapper.toUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/orders")
    public ResponseEntity<?> readUsersOrders(@PathVariable(value = "id") int id,
                                             @RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        params.put(OrderSearchParameters.USER_ID.getParameterName(),
                Collections.singletonList(Integer.toString(id)));
        List<Order> orders = orderService.read(params);
        CollectionModel<OrderDTO> orderDTOs = orderDTOMapper.toOrderDTOList(
                orders);
        Link link = linkTo(methodOn(this.getClass()).readUsersOrders(id, params)).withRel("tags");
        orderDTOs.add(link);
        Paginator paginator = new Paginator(params);

        if(paginator.isLimited(params)) {
            orderDTOs.add(linkTo(methodOn(this.getClass()).readUsersOrders(
                    id, paginator.nextPage(params))).withRel("nextPage"));
            orderDTOs.add(linkTo(methodOn(this.getClass()).readUsersOrders(
                    id, paginator.previousPage(params))).withRel("previousPage"));
        }
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<?> readOrder(@PathVariable(value = "id") int orderId) throws NotFoundException {
        OrderDTO order = orderDTOMapper.toOrderDTO(orderService.read(orderId));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/orders/{order_id}")
    public ResponseEntity<?> readOrder(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "order_id") int orderId)
            throws NotFoundException {
        OrderDTO order = orderDTOMapper.toOrderDTO(orderService.read(orderId));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}")
    public ResponseEntity<?> createOrder(@PathVariable(value = "id") int id,
                                         @RequestParam int certificateId,
                                         @RequestParam int quantity)
            throws NotFoundException, ServiceException, ValidationException {
        User user = userService.read(id);
        Certificate certificate = certificateService.read(certificateId);
        Order order = orderService.createOrder(certificate, user, quantity);
        return new ResponseEntity<>(orderDTOMapper.toOrderDTO(order), HttpStatus.OK);
    }
}
