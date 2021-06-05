package com.epam.esm.persistence.dao.order;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.QOrder;
import com.epam.esm.persistence.dao.EntityRepository;

/**
 * Order DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderDAO extends EntityRepository<Order, QOrder, Integer> {
}
