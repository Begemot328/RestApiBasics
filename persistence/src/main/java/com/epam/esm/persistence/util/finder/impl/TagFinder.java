package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link Tag} objects.
 * via {@link com.epam.esm.persistence.dao.tag.TagDAO} data sources.
 *
 * @author Yury Zmushko
 * @version 2.0
 */

@Component
@Scope("prototype")
public class TagFinder extends EntityFinder<Tag> {

    /**
     * Constructor.
     *
     * @param dao {@link EntityDAO} object to obtain {@link javax.persistence.criteria.CriteriaBuilder}
     *                             and {@link javax.persistence.metamodel.Metamodel objects}
     */
    public TagFinder(TagDAO dao) {
        super(dao);
    }

    @Override
    protected Class<Tag> getClassType() {
        return Tag.class;
    }

    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will include.
     */
    public void findByNameLike(String name) {
        add(builder.like(root.get(TagColumns.NAME.getValue()), "%" + name + "%"));
    }

    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will include.
     */
    public void findByName(String name) {
        add(builder.equal(root.get(TagColumns.NAME.getValue()), name));
    }
}
