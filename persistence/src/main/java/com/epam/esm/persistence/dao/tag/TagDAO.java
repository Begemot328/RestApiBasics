package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.QTag;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.ExCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public interface TagDAO extends /*CrudRepository<Tag, Integer>,*/
        ExCustomRepository<Tag, QTag, Integer>,
        TagDAOCustom {
}
