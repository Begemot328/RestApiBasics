package com.epam.esm.persistence.dao.tag;

import com.epam.esm.persistence.dao.AscDesc;
import com.epam.esm.persistence.dao.EntityFinder;
import com.epam.esm.model.entity.Tag;

public class TagFinder extends EntityFinder<Tag> {

    public TagFinder findByName(String name) {
        this.searchCondition = MySQLTagDAO.NAME + BLANK + LIKE.replace(VALUE,name);
        return this;
    }

    public TagFinder sortByName(AscDesc ascDesc) {
        return (TagFinder) sortBy(MySQLTagDAO.NAME, ascDesc);
    }

    public TagFinder findByCertificate(Tag tag) {
        this.searchCondition = MySQLTagDAO.CERTIFICATE_ID + EQUALS + tag.getId();
        return this;
    }
}
