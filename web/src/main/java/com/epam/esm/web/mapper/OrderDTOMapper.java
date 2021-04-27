package com.epam.esm.web.mapper;

import com.epam.esm.model.entity.Order;
import com.epam.esm.web.dto.OrderDTO;
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
        return mapper.map(order, OrderDTO.class);
    }

    public Order toOrder(OrderDTO orderDTO) {
        return mapper.map(orderDTO, Order.class);
    }
}
