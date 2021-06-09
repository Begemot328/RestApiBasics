package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.QOrder;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link Order} objects
 * via {@link com.epam.esm.persistence.dao.OrderDAO} datasources.
 *
 * @author Yury Zmushko
 * @version 1.0
 */

@Component
@Scope("prototype")
public class OrderFinder extends EntityFinder<Order> {

    public OrderFinder() {
        super();
    }

    /**
     * Find by User ID condition adding method.
     *
     * @param id User ID that found orders will include.
     */
    public void findByUser(int id) {
        add(QOrder.order.user.id.eq(id));
    }

    /**
     * Find by Certificate ID condition adding method.
     *
     * @param id Certificate ID that found orders will include.
     */
    public void findByCertificate(int id) {
        add(QOrder.order.certificate.id.eq(id));
    }

    /**
     * Find by ID condition adding method.
     *
     * @param id ID of the order.
     */
    public void findById(int id) {
        add(QOrder.order.id.eq(id));
    }
}
