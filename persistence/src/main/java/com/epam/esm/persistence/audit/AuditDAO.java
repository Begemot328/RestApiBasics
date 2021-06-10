package com.epam.esm.persistence.audit;

import com.epam.esm.persistence.audit.auditentity.AuditEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuditDAO extends CrudRepository<AuditEntity, Integer> {
}
