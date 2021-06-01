package com.epam.esm.persistence.dao.order;

import com.epam.esm.model.entity.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * Order DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface OrderDAO extends CrudRepository<Order, Integer>, OrderDAOCustom {
}
