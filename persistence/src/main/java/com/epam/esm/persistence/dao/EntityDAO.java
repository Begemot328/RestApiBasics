package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityDAO extends JpaRepository<CustomEntity, Integer> {
}
