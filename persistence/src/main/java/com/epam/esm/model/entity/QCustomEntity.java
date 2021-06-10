package com.epam.esm.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCustomEntity is a Querydsl query type for CustomEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QCustomEntity extends EntityPathBase<CustomEntity> {

    private static final long serialVersionUID = -1156248831L;

    public static final QCustomEntity customEntity = new QCustomEntity("customEntity");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QCustomEntity(String variable) {
        super(CustomEntity.class, forVariable(variable));
    }

    public QCustomEntity(Path<? extends CustomEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomEntity(PathMetadata metadata) {
        super(CustomEntity.class, metadata);
    }

}

