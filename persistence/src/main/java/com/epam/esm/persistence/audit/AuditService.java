package com.epam.esm.persistence.audit;

import com.epam.esm.persistence.audit.auditentity.AuditEntity;
import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.model.userdetails.Account;
import com.epam.esm.persistence.dao.EntityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditService {

    private AuditDAO dao;
    private EntityDAO entityDAO;
    private AuditorAware<Account> auditorAware;

    @Autowired
    public AuditService(AuditDAO dao, EntityDAO entityDAO, AuditorAware<Account> auditorAware) {
        this.dao = dao;
        this.entityDAO = entityDAO;
        this.auditorAware = auditorAware;
    }

    public AuditEntity createAudit(CustomEntity entity, String operation) {
        AuditEntity auditEntity = new AuditEntity(entity.getId(),
                entity.getClass().getSimpleName(), operation);
        auditEntity.setUser(auditorAware.getCurrentAuditor().orElseThrow(
                () -> new UsernameNotFoundException("User not found!")).getUser());
        return dao.save(auditEntity);
    }

    public Optional<CustomEntity> getOperationEntity(int id) {
        return entityDAO.findById(dao.findById(id).orElseThrow(
                () -> new RuntimeException("no such operation"))
                .getEntityId());
    }
}
