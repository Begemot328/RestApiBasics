package com.epam.esm.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -43070559L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final QCustomEntity _super = new QCustomEntity(this);

    public final QCertificate certificate;

    public final NumberPath<Integer> certificateQuantity = createNumber("certificateQuantity", Integer.class);

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final NumberPath<java.math.BigDecimal> orderAmount = createNumber("orderAmount", java.math.BigDecimal.class);

    public final DateTimePath<java.time.LocalDateTime> purchaseDate = createDateTime("purchaseDate", java.time.LocalDateTime.class);

    public final QUser user;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certificate = inits.isInitialized("certificate") ? new QCertificate(forProperty("certificate")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

