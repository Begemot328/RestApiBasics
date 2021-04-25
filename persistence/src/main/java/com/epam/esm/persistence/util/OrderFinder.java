package com.epam.esm.persistence.util;

import com.epam.esm.model.entity.Order;
import com.epam.esm.persistence.constants.OrderColumns;
import org.springframework.stereotype.Component;

@Component
public class OrderFinder extends EntityFinder<Order> {

    /**
     * Find by name condition adding method
     *
     * @param id string that found names will include
     */
    public void findByUser(int id) {
        find(OrderColumns.USER_ID.getValue() + " = " + id);
    }

}
