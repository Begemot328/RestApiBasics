package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.QOrder;
import com.epam.esm.model.entity.QTag;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OrderFinder extends EntityFinder<Order> {

    public OrderFinder() {
        super();
    }

    /**
     * Find by ID condition adding method
     *
     * @param id string that found names will include
     */
    public void findByUser(int id) {
        add(QOrder.order.user.id.eq(id));
    }

    public void findByCertificate(int id) {
        add(QOrder.order.certificate.id.eq(id));
    }

    public void findById(int id) {
        add(QOrder.order.id.eq(id));
    }
}
