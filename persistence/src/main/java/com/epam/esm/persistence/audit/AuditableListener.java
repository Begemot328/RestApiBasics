package com.epam.esm.persistence.audit;

import com.epam.esm.model.entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class AuditableListener {
    private static final String PERSIST = "PERSIST";
    private static final String UPDATE = "UPDATE";

    @Autowired
    private AuditService service;

    @PrePersist
    void preCreate(CustomEntity auditable) {
        service.createAudit(auditable, PERSIST);
    }

    @PreUpdate
    void preUpdate(CustomEntity auditable) {
        service.createAudit(auditable, UPDATE);
    }
}
