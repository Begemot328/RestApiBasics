package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.stereotype.Component;

@Component
public class OrderFinder extends EntityFinder<Order> {

    public OrderFinder(EntityDAO<Order> dao) {
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

    @Override
    protected Class<Order> getClassType() {
        return Order.class;
    }
}
