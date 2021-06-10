package com.epam.esm.persistence.audit.auditentity;

import com.epam.esm.model.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit")
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {
    @Id
    private int id;

    @Column(name = "entity_id", nullable = false)
    private int entityId;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @CreatedDate
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "operation", nullable = false)
    private String operation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AuditEntity(int entityId, String entityName, String operation) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.operation = operation;
    }

    public AuditEntity() {
        // Default constructor for JPA purposes
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
