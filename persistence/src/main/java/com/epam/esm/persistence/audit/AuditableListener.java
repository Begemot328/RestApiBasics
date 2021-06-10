package com.epam.esm.persistence.audit;

import com.epam.esm.model.entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

@Service
public class AuditableListener {
    private static final String PERSIST = "PERSIST";
    private static final String UPDATE = "UPDATE";

    private static AuditService service;

    @Autowired
    public AuditableListener(AuditService service) {
        this.service = service;
    }

    public AuditableListener() {

    }

    @PostPersist
    void preCreate(CustomEntity auditable) {
        service.createAudit(auditable, PERSIST);
    }



    @PreUpdate
    void preUpdate(CustomEntity auditable) {
        service.createAudit(auditable, UPDATE);
    }
}
