package com.epam.esm.web.dto.order;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.OrderController;
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

/**
 * {@link Order} to  {@link OrderDTO} mapper class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class OrderDTOMapper {
    private final ModelMapper mapper;

    /**
     * Constructor.
     *
     * @param mapper {@link ModelMapper} bean to add mapping.
     */
    @Autowired
    public OrderDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;

        Converter<User, UserDTO> userConverterToDTO =
                src -> new UserDTOMapper(this.mapper).toUserDTO(src.getSource());

        Converter<UserDTO, User> userConverterToObject =
                src -> new UserDTOMapper(this.mapper).toUser(src.getSource());

        Converter<Certificate, CertificateDTO> certificateConverterToDTO =
                src -> new CertificateDTOMapper(this.mapper).toCertificateDTO(src.getSource());

        Converter<CertificateDTO, Certificate> certificateConverterToObject =
                src -> new CertificateDTOMapper(this.mapper).toCertificate(src.getSource());

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

    /**
     * {@link Order} to  {@link OrderDTO} mapping.
     *
     * @param order {@link Order} to map.
     */
    public OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        try {
            Link link = linkTo(methodOn(OrderController.class).get(order.getId())).withSelfRel();
            orderDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return orderDTO;
    }

    /**
     * {@link List} of {@link Order} to {@link CollectionModel} of {@link OrderDTO} mapping.
     *
     * @param orders {@link List} of {@link Order} to map.
     * @return {@link CollectionModel} of {@link OrderDTO} objects.
     */
    public CollectionModel<OrderDTO> toOrderDTOList(List<Order> orders) {
        return CollectionModel.of(
                orders.stream().map(this::toOrderDTO).collect(Collectors.toList()));
    }

    /**
     * {@link OrderDTO} to  {@link Order} mapping.
     *
     * @param orderDTO {@link OrderDTO} to map.
     */
    public Order toOrder(OrderDTO orderDTO) {
        return mapper.map(orderDTO, Order.class);
    }
}
