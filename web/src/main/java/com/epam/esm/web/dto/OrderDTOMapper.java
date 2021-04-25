package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public OrderDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public OrderDTO toOrderDTO(Order order) {
        return Objects.isNull(order) ? null : mapper.map(order, OrderDTO.class);
    }

    public Order toOrder(OrderDTO orderDTO) {
        return Objects.isNull(orderDTO) ? null : mapper.map(orderDTO, Order.class);
    }
}
