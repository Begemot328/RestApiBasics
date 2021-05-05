package com.epam.esm.web.dto.order;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.dto.certificate.CertificateDTO;
import com.epam.esm.web.dto.certificate.CertificateDTOMapper;
import com.epam.esm.web.dto.user.UserDTO;
import com.epam.esm.web.dto.user.UserDTOMapper;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.Converter;
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
    private final ModelMapper mapper;

    @Autowired
    public OrderDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;

        Converter<User, UserDTO> userConverterToDTO =
                src ->  new UserDTOMapper(this.mapper).toUserDTO(src.getSource());

        Converter<UserDTO, User> userConverterToObject =
                src ->  new UserDTOMapper(this.mapper).toUser(src.getSource());

        Converter<Certificate, CertificateDTO> certificateConverterToDTO =
                src ->  new CertificateDTOMapper(this.mapper).toCertificateDTO(src.getSource());

        Converter<CertificateDTO, Certificate> certificateConverterToObject =
                src ->  new CertificateDTOMapper(this.mapper).toCertificate(src.getSource());

        mapper.typeMap(OrderDTO.class, Order.class).addMappings(
                newMapper -> newMapper.using(certificateConverterToObject).map(
                        OrderDTO::getCertificate, Order::setCertificate));

        mapper.typeMap(OrderDTO.class, Order.class).addMappings(
                newMapper -> newMapper.using(userConverterToObject).map(
                        OrderDTO::getUser, Order::setUser));

        mapper.typeMap(Order.class, OrderDTO.class).addMappings(
                newMapper -> newMapper.using(certificateConverterToDTO).map(
                        Order::getCertificate, OrderDTO::setCertificate));

        mapper.typeMap(Order.class, OrderDTO.class).addMappings(
                newMapper -> newMapper.using(userConverterToDTO).map(
                        Order::getUser, OrderDTO::setUser));
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
