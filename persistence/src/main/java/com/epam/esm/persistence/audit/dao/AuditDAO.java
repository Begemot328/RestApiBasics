package com.epam.esm.persistence.audit.dao;

import com.epam.esm.persistence.model.auditentity.AuditEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link AuditEntity} DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface AuditDAO extends CrudRepository<AuditEntity, Integer> {
}
