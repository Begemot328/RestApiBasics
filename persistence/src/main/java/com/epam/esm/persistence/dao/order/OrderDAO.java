package com.epam.esm.persistence.dao.order;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

public interface OrderDAO extends EntityDAO<Order> {
    /**
     * Find using {@link EntityFinder} method
     *
     * @param finder {@link EntityFinder} to obtain search parameters
     * @return {@link java.util.ArrayList}  of {@link Tag} objects
     */
    List<Order> readBy(EntityFinder<Order> finder);
}
