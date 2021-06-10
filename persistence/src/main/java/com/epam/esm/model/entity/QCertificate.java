package com.epam.esm.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCertificate is a Querydsl query type for Certificate
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCertificate extends EntityPathBase<Certificate> {

    private static final long serialVersionUID = 807320490L;

    public static final QCertificate certificate = new QCertificate("certificate");

    public final QCustomEntity _super = new QCustomEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> lastUpdateDate = createDateTime("lastUpdateDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final ListPath<Tag, QTag> tags = this.<Tag, QTag>createList("tags", Tag.class, QTag.class, PathInits.DIRECT2);

    public QCertificate(String variable) {
        super(Certificate.class, forVariable(variable));
    }

    public QCertificate(Path<? extends Certificate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCertificate(PathMetadata metadata) {
        super(Certificate.class, metadata);
    }

}

