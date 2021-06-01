package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.persistence.dao.order.OrderDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OrderFinder extends EntityFinder<Order> {

    public OrderFinder(OrderDAO dao) {
        super(dao);
    }

    /**
     * Find by ID condition adding method
     *
     * @param id string that found names will include
     */
    public void findByUser(int id) {
        add(builder.equal(root.join("user").get("id"), id));
    }

    public void findByCertificate(int id) {
        add(builder.equal(root.join("certificate").get("id"), id));
    }

    public void findById(int id) {
        add(builder.equal(root.get("id"), id));
    }

    @Override
    protected Class<Order> getClassType() {
        return Order.class;
    }
}
