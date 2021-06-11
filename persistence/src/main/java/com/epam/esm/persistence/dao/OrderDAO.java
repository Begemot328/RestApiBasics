package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.QOrder;

/**
 * Order DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderDAO extends EntityRepository<Order, QOrder, Integer> {
}
