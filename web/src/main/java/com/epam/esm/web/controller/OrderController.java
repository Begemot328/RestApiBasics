package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Order;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.order.OrderService;
import com.epam.esm.service.service.order.OrderServiceImpl;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.service.service.user.UserServiceImpl;
import com.epam.esm.web.dto.order.OrderDTO;
import com.epam.esm.web.dto.order.OrderDTOMapper;
import com.epam.esm.web.dto.user.UserDTOMapper;
import com.epam.esm.web.security.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders")
public class OrderController implements PaginableSearch {
    private final OrderService orderService;
    private final OrderDTOMapper orderDTOMapper;

    @Autowired
    public OrderController(OrderServiceImpl orderService,
                           OrderDTOMapper orderDTOMapper) {
        this.orderService = orderService;
        this.orderDTOMapper = orderDTOMapper;
    }

    @Secured(Roles.ADMIN)
    @GetMapping
    public ResponseEntity<?> find(@RequestParam MultiValueMap<String, String> params)
            throws NotFoundException, BadRequestException {
        List<Order> orders = orderService.findByParameters(params);
        CollectionModel<OrderDTO> orderDTOs = orderDTOMapper.toOrderDTOList(orders);
        orderDTOs.add(linkTo(methodOn(this.getClass()).find(params)).withRel("orders"));

        paginate(params, orderDTOs);

        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @Secured(Roles.ADMIN)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") int id) throws NotFoundException {
        OrderDTO order = orderDTOMapper.toOrderDTO(orderService.getById(id).orElseThrow(
                () -> new NotFoundException(String.format(orderService.notFoundErrorMessage, "id", id),
                        ErrorCodes.ORDER_NOT_FOUND)));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
