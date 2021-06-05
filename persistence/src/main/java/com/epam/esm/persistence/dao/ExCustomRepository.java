package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.CustomEntity;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExCustomRepository<T extends CustomEntity,
        P extends EntityPathBase<T>,
        ID extends Serializable>
        extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T>, QuerydslBinderCustomizer<P> {

    @Override
    default void customize(QuerydslBindings bindings, P root) {
    }

    default List<T> find(EntityFinder<T, P> finder) {
        return IterableUtils.toList(findAll(finder.getBooleanExpression()));
    }
}
