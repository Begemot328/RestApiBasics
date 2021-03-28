package com.epam.esm.persistence.dao.tag;

import com.epam.esm.persistence.dao.AscDesc;
import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagFinder extends EntityFinder<Tag> {

    public void findByName(String name) {
        this.searchCondition = TagDAO.NAME + BLANK + LIKE.replace(VALUE,name);
    }

    public void sortByName(AscDesc ascDesc) {
        sortBy(TagDAO.NAME, ascDesc);
    }

    public TagFinder findByCertificate(Tag tag) {
        this.searchCondition = TagDAO.CERTIFICATE_ID + EQUALS + tag.getId();
        return this;
    }
}
