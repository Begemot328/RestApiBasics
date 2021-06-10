package com.epam.esm.persistence.audit.auditentity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuditEntity is a Querydsl query type for AuditEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuditEntity extends EntityPathBase<AuditEntity> {

    private static final long serialVersionUID = 18396963L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuditEntity auditEntity = new QAuditEntity("auditEntity");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Integer> entityId = createNumber("entityId", Integer.class);

    public final StringPath entityName = createString("entityName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath operation = createString("operation");

    public final com.epam.esm.model.entity.QUser user;

    public QAuditEntity(String variable) {
        this(AuditEntity.class, forVariable(variable), INITS);
    }

    public QAuditEntity(Path<? extends AuditEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuditEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuditEntity(PathMetadata metadata, PathInits inits) {
        this(AuditEntity.class, metadata, inits);
    }

    public QAuditEntity(Class<? extends AuditEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.epam.esm.model.entity.QUser(forProperty("user")) : null;
    }

}

