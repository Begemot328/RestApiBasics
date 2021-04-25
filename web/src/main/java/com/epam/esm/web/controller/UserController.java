package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.CertificateService;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.service.service.UserService;
import com.epam.esm.service.service.impl.OrderServiceImpl;
import com.epam.esm.service.service.impl.UserServiceImpl;
import com.epam.esm.web.dto.OrderDTO;
import com.epam.esm.web.dto.OrderDTOMapper;
import com.epam.esm.web.dto.UserDTO;
import com.epam.esm.web.dto.UserDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
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

    @GetMapping
    public ResponseEntity<?> readAll()
            throws NotFoundException {
        List<UserDTO> users = userService.readAll()
                .stream().map(userDTOMapper::toUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> readUser(@PathVariable(value = "id") int id) throws NotFoundException {
        User user = userService.read(id);
        final UserDTO userDTO = userDTOMapper.toUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<?> readUsersOrders(@PathVariable(value = "id") int id) throws NotFoundException {
        List<OrderDTO> orders = orderService.readByUser(id)
                .stream().map(orderDTOMapper::toOrderDTO).collect(Collectors.toList());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/orders/{order_id}")
    public ResponseEntity<?> readOrder(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "order_id") int orderId)
            throws NotFoundException {
        OrderDTO order = orderDTOMapper.toOrderDTO(orderService.read(id));
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
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
