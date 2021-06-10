package com.epam.esm.persistence.util.finder.impl;

import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.model.entity.QTag;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.util.finder.EntityFinder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search criteria class to find {@link Tag} objects.
 * via {@link TagDAO} data sources.
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
     */
    public TagFinder() {
        super();
    }

    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will include.
     */
    public void findByNameLike(String name) {
        add(QTag.tag.name.containsIgnoreCase(name));
    }

    /**
     * Find by name condition adding method.
     *
     * @param name String that found names will be equal.
     */
    public void findByName(String name) {
        add(QTag.tag.name.eq(name));
    }
}
