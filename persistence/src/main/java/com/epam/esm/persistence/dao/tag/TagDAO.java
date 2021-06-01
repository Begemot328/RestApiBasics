package com.epam.esm.persistence.dao.tag;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * Tag DAO interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface TagDAO extends CrudRepository<Tag, Integer>, TagDAOCustom {
}
