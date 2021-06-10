package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.model.entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link CustomEntity} DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface EntityDAO extends JpaRepository<CustomEntity, Integer> {
}
