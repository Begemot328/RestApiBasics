package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.QTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.dao.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Repository
public interface TagDAO extends EntityRepository<Tag, QTag, Integer>,
        TagDAOCustom {

    Optional<Tag> getByName(String name);
}
