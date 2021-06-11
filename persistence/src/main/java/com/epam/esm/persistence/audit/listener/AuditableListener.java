package com.epam.esm.persistence.audit.listener;

import com.epam.esm.persistence.audit.service.AuditService;
import com.epam.esm.persistence.model.entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * {@link CustomEntity} operations listener.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class AuditableListener {
    private static final String PERSIST = "PERSIST";
    private static final String UPDATE = "UPDATE";
    private static final String REMOVE = "REMOVE";

    private static AuditService service;

    /**
     * Constructor.
     *
     * @param service  {@link AuditService} object.
     */
    @Autowired
    public AuditableListener(AuditService service) {
        this.service = service;
    }

    /**
     * Constructor.
     */
    public AuditableListener() {
        // Empty Constructor for Spring purposes.
    }

    /**
     * Post-creation processing.
     *
     * @param auditable  {@link CustomEntity} object to audit.
     */
    @PostPersist
    void preCreate(CustomEntity auditable) {
        service.createAudit(auditable, PERSIST);
    }

    /**
     * Pre-updating processing.
     *
     * @param auditable  {@link CustomEntity} object to audit.
     */
    @PreUpdate
    void preUpdate(CustomEntity auditable) {
        service.createAudit(auditable, UPDATE);
    }

    /**
     * Pre-removing processing.
     *
     * @param auditable  {@link CustomEntity} object to audit.
     */
    @PreRemove
    void preDestroy(CustomEntity auditable) {
        service.createAudit(auditable, REMOVE);
    }
}
