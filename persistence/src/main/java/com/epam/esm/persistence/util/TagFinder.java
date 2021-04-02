package com.epam.esm.persistence.util;

import com.epam.esm.persistence.constants.TagColumns;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagFinder extends EntityFinder<Tag> {

    public void findByName(String name) {
        this.searchCondition = TagColumns.NAME.getValue() + " " + FinderQuerries.LIKE.getValue()
                .replace(FinderQuerries.VALUE.getValue(),name);
    }

    public TagFinder findByCertificate(int certificateId) {
        this.searchCondition = TagColumns.CERTIFICATE_ID.getValue()
                + " = " + certificateId;
        return this;
    }
}
