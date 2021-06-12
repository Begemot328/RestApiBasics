package com.epam.esm.persistence.audit.service;

import com.epam.esm.persistence.audit.dao.AuditDAO;
import com.epam.esm.persistence.model.auditentity.AuditEntity;
import com.epam.esm.persistence.model.entity.CustomEntity;
import com.epam.esm.persistence.model.userdetails.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * {@link AuditEntity} service.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
@Transactional
public class AuditService {

    private AuditDAO dao;
    private AuditorAware<Account> auditorAware;

    /**
     * Constructor.
     *
     * @param dao  DAO to work with {@link AuditEntity} objects.
     * @param auditorAware {@link AuditorAware} to obtain current
     * {@link org.springframework.security.core.userdetails.UserDetails}.
     */
    @Autowired
    public AuditService(AuditDAO dao, AuditorAware<Account> auditorAware) {
        this.dao = dao;
        this.auditorAware = auditorAware;
    }

    /**
     * {@link AuditEntity} create and persist method.
     *
     * @param entity {@link CustomEntity} object to audit.
     * @param entity {@link String} name of the operation to audit.
     */
    public AuditEntity createAudit(CustomEntity entity, String operation) {
        AuditEntity auditEntity = new AuditEntity(entity.getId(),
                entity.getClass().getSimpleName(), operation);
        Optional<Account> account = auditorAware.getCurrentAuditor();
        if (account.isPresent()) {
            auditEntity.setUser(auditorAware.getCurrentAuditor().orElseThrow(
                    () -> new UsernameNotFoundException("User not found!")).getUser());
        }
        dao.save(auditEntity);
        return dao.save(auditEntity);
    }
}