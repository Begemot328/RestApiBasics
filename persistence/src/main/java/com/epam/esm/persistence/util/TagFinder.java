package com.epam.esm.persistence.util;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.persistence.constants.TagColumns;
import org.springframework.stereotype.Component;

@Component
public class TagFinder extends EntityFinder<Tag> {

    /**
     * Find by name condition adding method
     *
     * @param name string that found names will include
     */
    public void findByName(String name) {
        find(TagColumns.NAME.getValue() + " " + FinderQueries.LIKE.getValue()
                .replace(FinderQueries.VALUE.getValue(), name));
    }

    /**
     * Find by certificate id condition adding method
     *
     * @param certificateId id of the certificate to find by
     * @return search condition
     */
    public TagFinder findByCertificate(int certificateId) {
        this.searchCondition = TagColumns.CERTIFICATE_ID.getValue()
                + " = " + certificateId;
        return this;
    }
}
