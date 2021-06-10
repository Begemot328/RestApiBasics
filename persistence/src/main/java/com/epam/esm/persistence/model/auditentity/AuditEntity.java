package com.epam.esm.persistence.model.auditentity;

import com.epam.esm.persistence.model.entity.CustomEntity;
import com.epam.esm.persistence.model.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AuditEntity class for JPA audit.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Entity
@Table(name = "audit")
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * Constructor.
     *
     * @param entityId   {@link com.epam.esm.persistence.model.entity.CustomEntity} ID.
     * @param entityName  {@link com.epam.esm.persistence.model.entity.CustomEntity} class name.
     * @param operation Operation name.
     */
    public AuditEntity(int entityId, String entityName, String operation) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.operation = operation;
    }

    /**
     * Default constructor.
     */
    public AuditEntity() {
        // Default constructor for JPA purposes
    }

    /**
     * ID getter.
     *
     * @return  ID of the entity.
     */
    public int getId() {
        return id;
    }

    /**
     * ID setter.
     *
     * @param id ID of the entity.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@link CustomEntity} ID getter.
     *
     * @return  ID of the {@link CustomEntity}.
     */
    public int getEntityId() {
        return entityId;
    }

    /**
     * {@link CustomEntity} ID setter.
     *
     * @param entityId ID of the {@link CustomEntity}.
     */
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    /**
     * {@link CustomEntity} name getter.
     *
     * @return  Name of the {@link CustomEntity}.
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * {@link CustomEntity} name setter.
     *
     * @param entityName name of the {@link CustomEntity}.
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Date getter.
     *
     * @return Date of the operation.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Date setter.
     *
     * @param date Date of the operation.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Operation name getter.
     *
     * @return Name of the operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Operation name setter.
     *
     * @param operation Name of the operation.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * {@link User} getter.
     *
     * @return {@link User} who made the operation.
     */
    public User getUser() {
        return user;
    }

    /**
     * {@link User} setter.
     *
     * @param user {@link User} who made the operation.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Equality check.
     *
     * @param o Object to check equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEntity that = (AuditEntity) o;
        return id == that.id && entityId == that.entityId && Objects.equals(entityName, that.entityName) && Objects.equals(date, that.date) && Objects.equals(operation, that.operation) && Objects.equals(user, that.user);
    }

    /**
     * Hash code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, entityId, entityName, date, operation, user);
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
    @Override
    public String toString() {
        return "AuditEntity{" +
                "id=" + id +
                ", entityId=" + entityId +
                ", entityName='" + entityName + '\'' +
                ", date=" + date +
                ", operation='" + operation + '\'' +
                ", user=" + user +
                '}';
    }
}
