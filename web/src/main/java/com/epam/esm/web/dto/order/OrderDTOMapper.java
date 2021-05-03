package com.epam.esm.web.dto.order;

import com.epam.esm.model.entity.Order;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public OrderDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        try {
            Link link = linkTo(methodOn(UserController.class).readOrder(order.getId())).withSelfRel();
            orderDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return orderDTO;
    }

    public CollectionModel<OrderDTO> toOrderDTOList(List<Order> orders) {
        return CollectionModel.of(
                orders.stream().map(this::toOrderDTO).collect(Collectors.toList()));
    }

    public Order toOrder(OrderDTO orderDTO) {
        return mapper.map(orderDTO, Order.class);
    }
}
